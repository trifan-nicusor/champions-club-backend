package ro.championsclub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.championsclub.dto.response.CartView;
import ro.championsclub.entity.Cart;
import ro.championsclub.entity.Discount;
import ro.championsclub.entity.Subscription;
import ro.championsclub.entity.User;
import ro.championsclub.exception.BusinessException;
import ro.championsclub.mapper.ModelMapper;
import ro.championsclub.repository.CartRepository;
import ro.championsclub.repository.DiscountRepository;
import ro.championsclub.repository.SubscriptionRepository;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final DiscountService discountService;
    private final DiscountRepository discountRepository;

    @PreAuthorize("hasAuthority('USER')")
    public CartView getMyCart(User user) {
        var cart = getCartByUser(user);

        return ModelMapper.map(cart, CartView.class);
    }

    @PreAuthorize("hasAuthority('USER')")
    public void addSubscription(User user, String name) {
        var subscription = subscriptionRepository.getByName(name);
        var cart = getCartByUser(user);

        cart.getSubscriptions().add(subscription);

        cartRepository.save(cart);
    }

    @Transactional
    @PreAuthorize("hasAuthority('USER')")
    public CartView removeSubscription(User user, String name) {
        var subscription = subscriptionRepository.getByName(name);
        var cart = getCartByUser(user);

        cart.getSubscriptions().remove(subscription);

        refreshCart(cart);

        return ModelMapper.map(cart, CartView.class);
    }

    @PreAuthorize("hasAuthority('USER')")
    public CartView addDiscount(User user, String code) {
        var cart = getCartByUser(user);
        var discount = discountRepository.getByCode(code);

        Set<Discount> discounts = cart.getDiscounts();
        BigDecimal minimumCartTotal = discount.getMinimumCartTotal();

        if (minimumCartTotal.compareTo(cart.getTotal()) > 0) {
            throw new BusinessException("Cart total must be at least: " + minimumCartTotal);
        }

        if (discounts.isEmpty()) {
            cart.getDiscounts().add(discount);
        } else if (!discount.getCompatibleWithOther()) {
            throw new BusinessException("Discount not compatible with other discounts");
        } else if (discounts.size() == 1) {
            var getDiscount = discounts.stream().findFirst().get();

            if (!getDiscount.getCompatibleWithOther()) {
                throw new BusinessException("Cart contains a discount incompatible with other discounts");
            }
        }

        cart.getDiscounts().add(discount);
        cartRepository.save(cart);

        refreshCart(cart);

        return ModelMapper.map(cart, CartView.class);
    }

    @PreAuthorize("hasAuthority('USER')")
    public CartView removeDiscount(User user, String code) {
        var cart = getCartByUser(user);
        var discount = discountRepository.getByCode(code);

        cart.getDiscounts().remove(discount);

        cartRepository.save(cart);

        refreshCart(cart);

        return ModelMapper.map(cart, CartView.class);
    }

    public Cart getCartByUser(User user) {
        var cart = cartRepository.findByUser(user).orElseGet(() -> buildAndSaveCart(user));

        return refreshCart(cart);
    }

    private Cart refreshCart(Cart cart) {
        if (cart.getSubscriptions().isEmpty()) {
            cart.getDiscounts().clear();
            cart.setTotal(BigDecimal.ZERO);
            cart.setDiscount(BigDecimal.ZERO);

            return cartRepository.save(cart);
        }

        Set<Discount> discounts = cart.getDiscounts()
                .stream()
                .filter(discount -> discount.getMinimumCartTotal().compareTo(cart.getTotal()) < 0)
                .collect(Collectors.toSet());

        BigDecimal cartTotal = cart.getSubscriptions()
                .stream()
                .map(Subscription::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal discountTotal = discountService.calculateDiscount(discounts, cartTotal);

        cart.setTotal(cartTotal);
        cart.setDiscount(discountTotal);
        cart.setTotal(cartTotal.subtract(discountTotal));
        cart.setDiscounts(discounts);

        return cartRepository.save(cart);
    }

    private Cart buildAndSaveCart(User user) {
        var cart = Cart.builder()
                .user(user)
                .build();

        return cartRepository.save(cart);
    }

}

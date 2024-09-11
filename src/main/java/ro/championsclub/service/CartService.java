package ro.championsclub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ro.championsclub.dto.response.CartView;
import ro.championsclub.entity.Cart;
import ro.championsclub.entity.Discount;
import ro.championsclub.entity.Product;
import ro.championsclub.entity.User;
import ro.championsclub.exception.BusinessException;
import ro.championsclub.mapper.ModelMapper;
import ro.championsclub.repository.CartRepository;
import ro.championsclub.repository.DiscountRepository;
import ro.championsclub.repository.ProductRepository;
import ro.championsclub.repository.SubscriptionRepository;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ProductRepository productRepository;
    private final DiscountService discountService;
    private final DiscountRepository discountRepository;

    @PreAuthorize("hasAuthority('USER')")
    public CartView getMyCart(User user) {
        var cart = getCartByUser(user);

        return ModelMapper.map(cart, CartView.class);
    }

    @PreAuthorize("hasAuthority('USER')")
    public void addSubscription(User user, String subName) {
        var subscription = subscriptionRepository.getByName(subName);
        var cart = getCartByUser(user);

        if (cart.getProducts().isEmpty()) {
            cart.getSubscriptions().add(subscription);
            cart.setTotal(subscription.getPrice());
        } else if (!cart.getSubscriptions().contains(subscription)) {
            var product = Product.builder()
                    .subscription(subscription)
                    .cart(cart)
                    .build();

            cart.getProducts().add(product);
        }

        cartRepository.save(cart);
    }

    @PreAuthorize("hasAuthority('USER')")
    public CartView removeSubscription(User user, String subName) {
        var subscription = subscriptionRepository.getByName(subName);
        var cart = getCartByUser(user);

        if (!cart.getProducts().isEmpty()) {
            productRepository.removeProduct(cart.getId(), subscription.getId());
        }

        refreshCart(cart);

        return ModelMapper.map(cart, CartView.class);
    }

    @PreAuthorize("hasAuthority('USER')")
    public CartView addDiscount(User user, String code) {
        var cart = getCartByUser(user);
        var discount = discountRepository.getByCode(code);

        Set<Discount> cartDiscounts = cart.getDiscounts();

        if (discount.getMinimumCartTotal().compareTo(cart.getTotal()) > 0) {
            throw new BusinessException("Cart total must be at least: " + discount.getMinimumCartTotal());
        }

        if (cartDiscounts.isEmpty()) {
            cart.getDiscounts().add(discount);
            cart.setDiscount(discountService.calculateDiscount(cartDiscounts, cart.getTotal()));
        } else if (!discount.getCompatibleWithOther()) {
            throw new BusinessException("Discount not compatible with other discounts");
        }

        cart.getDiscounts().add(discount);

        refreshCart(cart);

        return ModelMapper.map(cart, CartView.class);
    }

    @PreAuthorize("hasAuthority('USER')")
    public CartView removeDiscount(User user, String code) {
        var cart = getCartByUser(user);
        var discount = discountRepository.getByCode(code);

        cart.getDiscounts().remove(discount);

        cartRepository.save(cart);

        return ModelMapper.map(cart, CartView.class);
    }

    private Cart getCartByUser(User user) {
        var cart = cartRepository.findByUser(user).orElseGet(() -> buildAndSaveCart(user));

        return refreshCart(cart);
    }

    private Cart refreshCart(Cart cart) {
        Set<Discount> discounts = cart.getDiscounts()
                .stream()
                .filter(discount -> discount.getMinimumCartTotal().compareTo(cart.getTotal()) < 0)
                .collect(Collectors.toSet());

        BigDecimal productTotal = cart.getProducts()
                .stream()
                .map(product -> product.getSubscription().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal discountTotal = discountService.calculateDiscount(discounts, productTotal);

        cart.setTotal(productTotal);
        cart.setDiscount(discountTotal);
        cart.setTotal(productTotal.subtract(discountTotal));
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
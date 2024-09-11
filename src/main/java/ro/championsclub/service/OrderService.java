package ro.championsclub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ro.championsclub.dto.response.OrderDiscountResponse;
import ro.championsclub.dto.response.OrderProductResponse;
import ro.championsclub.dto.response.OrderResponse;
import ro.championsclub.entity.Order;
import ro.championsclub.entity.OrderDiscount;
import ro.championsclub.entity.OrderProduct;
import ro.championsclub.entity.User;
import ro.championsclub.repository.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderDiscountRepository orderDiscountRepository;
    private final OrderProductRepository orderProductRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;

    public void createOrder(String userEmail) {
        var user = userRepository.getByEmail(userEmail);
        var cart = cartService.getCartByUser(user);

        var order = Order.builder()
                .user(user)
                .cart(cart)
                .build();

        orderRepository.save(order);

        BigDecimal discountValue = cart.getDiscount().divide(BigDecimal.valueOf(cart.getProducts().size()), RoundingMode.CEILING);

        Set<OrderProduct> productsForOrder = cart.getProducts().stream()
                .map(product -> {
                    var subscription = product.getSubscription();
                    int durationInDays = subscription.getDurationInMonths() * 30;

                    return OrderProduct.builder()
                            .name(subscription.getName())
                            .price(subscription.getPrice().subtract(discountValue))
                            .validFrom(LocalDate.now())
                            .validTo(LocalDate.now().plusDays(durationInDays))
                            .endingHour(subscription.getEndingHour())
                            .startingHour(subscription.getStartingHour())
                            .durationInMonths(subscription.getDurationInMonths())
                            .order(order)
                            .build();
                }).collect(Collectors.toSet());
        orderProductRepository.saveAll(productsForOrder);

        Set<OrderDiscount> discountsForOrder = cart.getDiscounts().stream()
                .map(discount -> OrderDiscount.builder()
                        .name(discount.getName())
                        .code(discount.getCode())
                        .value(discount.getValue())
                        .order(order)
                        .type(discount.getType())
                        .build()
                ).collect(Collectors.toSet());
        orderDiscountRepository.saveAll(discountsForOrder);

        cartRepository.deleteById(Math.toIntExact(cart.getId()));
    }

    @PreAuthorize("hasAuthority('USER')")
    public List<OrderResponse> getMyOrders(User user) {
        List<Order> orders = orderRepository.findAllByUser(user);

        return orders.stream()
                .map(order -> new OrderResponse(
                                order.getId(),
                                order.getStatus().name(),
                                order.getPaymentMethod().name(),
                                order.getOrderProducts().stream()
                                        .map(product -> new OrderProductResponse(
                                                        product.getName(),
                                                        product.getPrice(),
                                                        product.getDurationInMonths(),
                                                        product.getStartingHour(),
                                                        product.getEndingHour(),
                                                        product.getValidFrom(),
                                                        product.getValidTo()
                                                )
                                        ).collect(Collectors.toSet()),
                                order.getOrderDiscounts().stream()
                                        .map(discount -> new OrderDiscountResponse(
                                                        discount.getCode(),
                                                        discount.getType(),
                                                        discount.getValue()
                                                )
                                        ).collect(Collectors.toSet())
                        )
                ).toList();
    }

}

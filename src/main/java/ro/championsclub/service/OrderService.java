package ro.championsclub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.championsclub.dto.response.OrderView;
import ro.championsclub.entity.Order;
import ro.championsclub.entity.OrderDiscount;
import ro.championsclub.entity.OrderSubscriptions;
import ro.championsclub.entity.User;
import ro.championsclub.mapper.ModelMapper;
import ro.championsclub.repository.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderDiscountRepository orderDiscountRepository;
    private final OrderProductRepository orderProductRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;

    @PreAuthorize("hasAuthority('USER')")
    public List<OrderView> getMyOrders(User user) {
        List<Order> orders = orderRepository.findAllByUser(user);

        orders.forEach(order -> {
            System.out.println(order.getOrderSubscriptions());
            System.out.println(order.getOrderDiscounts());
        });

        return ModelMapper.mapAll(orders, OrderView.class);
    }

    @Transactional
    public void createOrder(String email) {
        var user = userRepository.getByEmail(email);
        var cart = cartService.getCartByUser(user);

        var order = Order.builder()
                .user(user)
                .cart(cart)
                .build();

        orderRepository.save(order);

        var subscriptionsNumber = BigDecimal.valueOf(cart.getSubscriptions().size());
        var discountValue = cart.getDiscount().divide(subscriptionsNumber, RoundingMode.CEILING);

        cart.getSubscriptions().forEach(sub -> sub.setPrice(sub.getPrice().subtract(discountValue)));

        List<OrderSubscriptions> orderSubscription = ModelMapper.mapAll(cart.getSubscriptions(), OrderSubscriptions.class);
        orderSubscription.forEach(orderProduct -> orderProduct.setOrder(order));
        orderProductRepository.saveAll(orderSubscription);

        List<OrderDiscount> orderDiscounts = ModelMapper.mapAll(cart.getDiscounts(), OrderDiscount.class);
        orderDiscounts.forEach(orderDiscount -> orderDiscount.setOrder(order));
        orderDiscountRepository.saveAll(orderDiscounts);

        cartRepository.setCartOrdered(cart.getId());
    }

}

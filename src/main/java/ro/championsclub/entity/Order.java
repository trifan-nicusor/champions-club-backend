package ro.championsclub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ro.championsclub.constant.OrderStatusEnum;
import ro.championsclub.constant.PaymentMethodEnum;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "cc", name = "order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Builder.Default
    @Column(
            name = "status",
            nullable = false,
            length = 32
    )
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status = OrderStatusEnum.SUCCESSFUL;

    @Builder.Default
    @Column(
            name = "payment_method",
            nullable = false,
            length = 32
    )
    @Enumerated(EnumType.STRING)
    private PaymentMethodEnum paymentMethod = PaymentMethodEnum.CARD;

    @NotNull
    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private LocalDateTime orderedAt = LocalDateTime.now();

    // relationships
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "user_id"
    )
    private User user;

    @OneToOne
    @JoinColumn(
            nullable = false,
            name = "cart_id"
    )
    private Cart cart;

    @Builder.Default
    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private Set<OrderDiscount> orderDiscounts = new HashSet<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private Set<OrderProduct> orderProducts = new HashSet<>();

}
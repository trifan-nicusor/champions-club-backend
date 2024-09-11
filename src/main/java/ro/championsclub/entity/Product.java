package ro.championsclub.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "cc", name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Long id;

    // relationships
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "subscription_id"
    )
    private Subscription subscription;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "cart_id"
    )
    private Cart cart;

}
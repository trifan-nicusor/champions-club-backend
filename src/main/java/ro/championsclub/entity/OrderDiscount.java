package ro.championsclub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ro.championsclub.constant.DiscountTypeEnum;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "cc", name = "order_discount")
public class OrderDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank
    @Size(max = 32)
    @Column(name = "name", nullable = false, length = 32)
    private String name;

    @NotBlank
    @Size(max = 32)
    @Column(name = "code", nullable = false, length = 16)
    private String code;

    @NotNull
    @Column(name = "value", nullable = false)
    private Integer value;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private DiscountTypeEnum type;

    // relationships
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "order_id"
    )
    private Order order;

}

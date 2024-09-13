package ro.championsclub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import ro.championsclub.constant.DiscountTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "cc", name = "discount")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private int id;

    @NotBlank
    @Size(max = 32)
    @Column(
            name = "name",
            unique = true,
            nullable = false,
            length = 32
    )
    private String name;

    @NotBlank
    @Size(max = 16)
    @Column(
            name = "code",
            unique = true,
            nullable = false,
            length = 16
    )
    private String code;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 32)
    private DiscountTypeEnum type;

    @NotNull
    @Min(0)
    @Column(name = "value", nullable = false)
    private Integer value;

    @NotNull
    @Min(0)
    @Column(name = "use_per_user", nullable = false)
    private Integer usePerUser;

    @NotNull
    @DecimalMin("0")
    @Column(name = "minimum_cart_total", nullable = false)
    private BigDecimal minimumCartTotal;

    @NotNull
    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    @NotNull
    @Column(name = "valid_to", nullable = false)
    private LocalDate validTo;

    @NotNull
    @Column(name = "compatible_with_other", nullable = false)
    private Boolean compatibleWithOther;

    @NotNull
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @NotNull
    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}

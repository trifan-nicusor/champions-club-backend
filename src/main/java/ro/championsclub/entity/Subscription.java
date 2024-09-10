package ro.championsclub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE cc.subscription SET is_active = false WHERE id = ?")
@Table(schema = "cc", name = "subscription")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Integer id;

    @NotBlank
    @Size(max = 32)
    @Column(
            name = "name",
            unique = true,
            nullable = false,
            length = 32
    )
    private String name;

    @NotNull
    @DecimalMin("0")
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @NotNull
    @Min(1)
    @Max(12)
    @Column(name = "duration_in_months", nullable = false)
    private Integer durationInMonths;

    @NotNull
    @Min(0)
    @Column(name = "starting_hour", nullable = false)
    private Integer startingHour;

    @NotNull
    @Min(1)
    @Max(24)
    @Column(name = "ending_hour", nullable = false)
    private Integer endingHour;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @NotNull
    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // relationships
    @OneToOne
    @JoinColumn(
            nullable = false,
            name = "image_id"
    )
    private Image image;

}

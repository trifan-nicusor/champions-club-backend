package ro.championsclub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "cc", name = "order_subscription")
public class OrderSubscriptions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Size(max = 32)
    @Column(name = "name", nullable = false, length = 32)
    private String name;

    @NotNull
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @NotNull
    @Column(name = "duration_in_months", nullable = false)
    private Integer durationInMonths;

    @NotNull
    @Column(name = "starting_hour", nullable = false)
    private Integer startingHour;

    @NotNull
    @Column(name = "ending_hour", nullable = false)
    private Integer endingHour;

    @NotNull
    @Builder.Default
    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom = LocalDate.now();

    @NotNull
    @Column(name = "valid_to", nullable = false)
    private LocalDate validTo;

    // relationships
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "order_id"
    )
    private Order order;

}

package ro.championsclub.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SubscriptionRequest {

    @NotBlank
    @Size(max = 32)
    private String name;

    @NotNull
    @DecimalMin("0")
    private BigDecimal price;

    @NotNull
    @Min(1)
    private Integer durationInMonths;

    @NotNull
    @Min(0)
    private Integer startingHour;

    @NotNull
    @Min(1)
    @Max(24)
    private Integer endingHour;

}

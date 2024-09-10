package ro.championsclub.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SubscriptionUpdateRequest {

    @Size(max = 32)
    private String name;

    @DecimalMin("0")
    private BigDecimal price;

    @Min(1)
    private Integer durationInMonths;

    @Min(0)
    private Integer startingHour;

    @Min(1)
    @Max(24)
    private Integer endingHour;

}

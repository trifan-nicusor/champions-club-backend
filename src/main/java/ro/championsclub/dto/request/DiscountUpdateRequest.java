package ro.championsclub.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class DiscountUpdateRequest {

    @Size(max = 32)
    private String name;

    @Size(max = 16)
    private String code;

    @Size(max = 32)
    private String type;

    @Min(0)
    private Integer value;

    @Min(0)
    private Integer usePerUser;

    @DecimalMin("0")
    private BigDecimal minimumCartTotal;

    private LocalDate validFrom;
    private LocalDate validTo;
    private boolean compatibleWithOther;

}

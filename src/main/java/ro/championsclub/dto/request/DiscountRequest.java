package ro.championsclub.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class DiscountRequest {

    @NotBlank
    @Size(max = 32)
    private String name;

    @NotBlank
    @Size(max = 16)
    private String code;

    @NotBlank
    @Size(max = 32)
    private String type;

    @NotNull
    @Min(0)
    private Integer value;

    @NotNull
    @Min(0)
    private Integer usePerUser;

    @NotNull
    @DecimalMin("0")
    private BigDecimal minimumCartTotal;

    @NotNull
    private LocalDate validFrom;

    @NotNull
    private LocalDate validTo;

    @NotNull
    private Boolean compatibleWithOther;

}
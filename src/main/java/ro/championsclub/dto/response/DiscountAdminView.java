package ro.championsclub.dto.response;

import lombok.Getter;
import lombok.Setter;
import ro.championsclub.constant.DiscountTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class DiscountAdminView {

    private int id;
    private String name;
    private String code;
    private DiscountTypeEnum type;
    private int value;
    private int usePerUser;
    private BigDecimal minimumCartTotal;
    private LocalDate validFrom;
    private LocalDate validTo;
    private boolean compatibleWithOther;
    private LocalDateTime createdAt;

}

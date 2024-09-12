package ro.championsclub.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class OrderSubscriptionView {

    private String name;
    private BigDecimal price;
    private int durationInMonths;
    private int startingHour;
    private int endingHour;
    private LocalDate startingDate;
    private LocalDate endingDate;

}

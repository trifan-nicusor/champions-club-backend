package ro.championsclub.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OrderProductResponse(String name,
                                   BigDecimal price,
                                   int durationInMonths,
                                   int startingHour,
                                   int endingHour,
                                   LocalDate startingDate,
                                   LocalDate endingDate) {

}

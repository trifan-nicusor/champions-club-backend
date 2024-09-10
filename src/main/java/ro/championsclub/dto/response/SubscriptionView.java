package ro.championsclub.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SubscriptionView {

    private String subscriptionName;
    private BigDecimal price;
    private int durationInMonths;
    private int startingHour;
    private int endingHour;
    private String imageName;
    private String imageData;

}

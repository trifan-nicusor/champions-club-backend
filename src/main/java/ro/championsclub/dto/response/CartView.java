package ro.championsclub.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CartView {

    private BigDecimal total;
    private BigDecimal discount;
    private List<SubscriptionView> subscriptions;
    private List<DiscountView> discounts;

}

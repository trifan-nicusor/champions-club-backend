package ro.championsclub.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderView {

    private Long id;
    private String status;
    private String paymentMethod;
    private List<OrderSubscriptionView> subscriptions;
    private List<OrderDiscountView> discounts;

}

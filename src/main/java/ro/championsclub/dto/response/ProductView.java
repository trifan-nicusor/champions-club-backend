package ro.championsclub.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductView {

    private long id;
    private SubscriptionView subscription;

}
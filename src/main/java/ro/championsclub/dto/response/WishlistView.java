package ro.championsclub.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class WishlistView {

    private Set<SubscriptionView> subscriptions;

}

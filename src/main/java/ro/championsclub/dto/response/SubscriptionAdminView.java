package ro.championsclub.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SubscriptionAdminView extends SubscriptionView {

    private int id;
    private LocalDateTime createdAt;

}

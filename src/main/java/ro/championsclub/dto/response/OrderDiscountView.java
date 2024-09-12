package ro.championsclub.dto.response;

import lombok.Getter;
import lombok.Setter;
import ro.championsclub.constant.DiscountTypeEnum;

@Getter
@Setter
public class OrderDiscountView {

    private String code;
    private DiscountTypeEnum type;
    private int value;

}

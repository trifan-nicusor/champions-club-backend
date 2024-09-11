package ro.championsclub.dto.response;

import lombok.Getter;
import lombok.Setter;
import ro.championsclub.constant.DiscountTypeEnum;

@Getter
@Setter
public class DiscountView {

    private String name;
    private String code;
    private int value;
    private DiscountTypeEnum type;

}

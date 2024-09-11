package ro.championsclub.dto.response;

import ro.championsclub.constant.DiscountTypeEnum;

public record OrderDiscountResponse(String code,
                                    DiscountTypeEnum type,
                                    int value) {

}

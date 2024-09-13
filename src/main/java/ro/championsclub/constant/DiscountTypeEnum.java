package ro.championsclub.constant;

public enum DiscountTypeEnum {

    TOTAL_CART_VALUE,
    PERCENTAGE,
    UNDEFINED;

    public static DiscountTypeEnum getType(String type) {
        if (type == null) {
            return DiscountTypeEnum.UNDEFINED;
        }

        try {
            return DiscountTypeEnum.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return DiscountTypeEnum.UNDEFINED;
        }
    }

}

package ro.championsclub.constant;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountTypeEnumTest {

    @Test
    public void getTypeValidInputTest() {
        assertThat(DiscountTypeEnum.TOTAL_CART_VALUE).isEqualTo(DiscountTypeEnum.getType("TOTAL_CART_VALUE"));
        assertThat(DiscountTypeEnum.PERCENTAGE).isEqualTo(DiscountTypeEnum.getType("PERCENTAGE"));
    }

    @Test
    public void getTypeLowercaseInputTest() {
        assertThat(DiscountTypeEnum.TOTAL_CART_VALUE).isEqualTo(DiscountTypeEnum.getType("total_cart_value"));
        assertThat(DiscountTypeEnum.PERCENTAGE).isEqualTo(DiscountTypeEnum.getType("percentage"));
    }

    @Test
    public void getTypeInvalidInputTest() {
        assertThat(DiscountTypeEnum.UNDEFINED).isEqualTo(DiscountTypeEnum.getType("random"));
        assertThat(DiscountTypeEnum.UNDEFINED).isEqualTo(DiscountTypeEnum.getType("123456"));
    }

    @Test
    public void getTypeEmptyInputTest() {
        assertThat(DiscountTypeEnum.UNDEFINED).isEqualTo(DiscountTypeEnum.getType(""));
    }

    @Test
    public void getTypeNullInputTest() {
        assertThat(DiscountTypeEnum.UNDEFINED).isEqualTo(DiscountTypeEnum.getType(null));
    }

}

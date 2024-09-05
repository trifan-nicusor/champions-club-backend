package ro.championsclub.constant;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EquipmentCategoryEnumTest {

    @Test
    public void getCategoryValidInputTest() {
        assertThat(EquipmentCategoryEnum.CARDIO).isEqualTo(EquipmentCategoryEnum.getCategory("CARDIO"));
        assertThat(EquipmentCategoryEnum.STRENGTH).isEqualTo(EquipmentCategoryEnum.getCategory("STRENGTH"));
        assertThat(EquipmentCategoryEnum.FUNCTIONAL).isEqualTo(EquipmentCategoryEnum.getCategory("FUNCTIONAL"));
    }

    @Test
    public void getCategoryLowercaseInputTest() {
        assertThat(EquipmentCategoryEnum.CARDIO).isEqualTo(EquipmentCategoryEnum.getCategory("cardio"));
        assertThat(EquipmentCategoryEnum.STRENGTH).isEqualTo(EquipmentCategoryEnum.getCategory("strength"));
        assertThat(EquipmentCategoryEnum.FUNCTIONAL).isEqualTo(EquipmentCategoryEnum.getCategory("functional"));
    }

    @Test
    public void getCategoryInvalidInputTest() {
        assertThat(EquipmentCategoryEnum.UNDEFINED).isEqualTo(EquipmentCategoryEnum.getCategory("random"));
        assertThat(EquipmentCategoryEnum.UNDEFINED).isEqualTo(EquipmentCategoryEnum.getCategory("123456"));
    }

    @Test
    public void getCategoryEmptyInputTest() {
        assertThat(EquipmentCategoryEnum.UNDEFINED).isEqualTo(EquipmentCategoryEnum.getCategory(""));
    }

    @Test
    public void getCategoryNullInputTest() {
        assertThat(EquipmentCategoryEnum.UNDEFINED).isEqualTo(EquipmentCategoryEnum.getCategory(null));
    }

}

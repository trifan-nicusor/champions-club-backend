package ro.championsclub.constant;

public enum EquipmentCategoryEnum {

    CARDIO,
    STRENGTH,
    FUNCTIONAL,
    UNDEFINED;

    public static EquipmentCategoryEnum getCategory(String category) {
        if (category == null) {
            return EquipmentCategoryEnum.UNDEFINED;
        }

        try {
            return EquipmentCategoryEnum.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            return EquipmentCategoryEnum.UNDEFINED;
        }
    }

}

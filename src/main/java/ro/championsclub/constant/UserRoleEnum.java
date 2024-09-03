package ro.championsclub.constant;

public enum UserRoleEnum {

    USER,
    ADMIN,
    UNDEFINED;

    public static UserRoleEnum getType(String type) {
        if (type == null) {
            return UserRoleEnum.UNDEFINED;
        }

        try {
            return UserRoleEnum.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UserRoleEnum.UNDEFINED;
        }
    }

}

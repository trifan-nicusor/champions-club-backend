package ro.championsclub.constant;

public enum UserRoleEnum {

    USER,
    ADMIN,
    UNDEFINED;

    public static UserRoleEnum getType(String type) {
        try {
            return UserRoleEnum.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UserRoleEnum.UNDEFINED;
        }
    }

}
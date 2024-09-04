package ro.championsclub.constant;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserRoleEnumTest {

    @Test
    public void getTypeValidInputTest() {
        assertThat(UserRoleEnum.USER).isEqualTo(UserRoleEnum.getType("USER"));
        assertThat(UserRoleEnum.ADMIN).isEqualTo(UserRoleEnum.getType("ADMIN"));
    }

    @Test
    public void getTypeLowercaseInputTest() {
        assertThat(UserRoleEnum.USER).isEqualTo(UserRoleEnum.getType("user"));
        assertThat(UserRoleEnum.ADMIN).isEqualTo(UserRoleEnum.getType("admin"));
    }

    @Test
    public void getTypeInvalidInputTest() {
        assertThat(UserRoleEnum.UNDEFINED).isEqualTo(UserRoleEnum.getType("random"));
        assertThat(UserRoleEnum.UNDEFINED).isEqualTo(UserRoleEnum.getType("123456"));
    }

    @Test
    public void getTypeEmptyInputTest() {
        assertThat(UserRoleEnum.UNDEFINED).isEqualTo(UserRoleEnum.getType(""));
    }

    @Test
    public void getTypeNullInputTest() {
        assertThat(UserRoleEnum.UNDEFINED).isEqualTo(UserRoleEnum.getType(null));
    }

}

package ro.championsclub.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ro.championsclub.constant.EquipmentCategoryEnum;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class EquipmentUpdateRequestTest {

    private static Validator validator;

    @BeforeAll
    public static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validNameTest() {
        var request = new EquipmentUpdateRequest();
        request.setName("name");
        request.setCategory(EquipmentCategoryEnum.UNDEFINED);

        Set<ConstraintViolation<EquipmentUpdateRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }


    @Test
    public void exceedsMaxNameTest() {
        var request = new EquipmentUpdateRequest();
        request.setName("a".repeat(33));
        request.setCategory(EquipmentCategoryEnum.UNDEFINED);

        Set<ConstraintViolation<EquipmentUpdateRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("size must be between 0 and 32");
    }

}

package ro.championsclub.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class EquipmentRequestTest {

    private static Validator validator;

    @BeforeAll
    public static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validNameAndCategoryTest() {
        var request = new EquipmentRequest();
        request.setName("name");
        request.setCategory("category");

        Set<ConstraintViolation<EquipmentRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    public void blankNameAndCategoryTest() {
        var request = new EquipmentRequest();
        request.setName("");
        request.setCategory("");

        Set<ConstraintViolation<EquipmentRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(2);
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("must not be blank");
    }

    @Test
    public void exceedsMaxNameAndCategoryTest() {
        var request = new EquipmentRequest();
        request.setName("a".repeat(33));
        request.setCategory("a".repeat(33));

        Set<ConstraintViolation<EquipmentRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(2);
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("size must be between 0 and 32");
    }

}

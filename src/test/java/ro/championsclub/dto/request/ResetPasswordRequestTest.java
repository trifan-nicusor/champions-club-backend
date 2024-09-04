package ro.championsclub.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ResetPasswordRequestTest {

    private static Validator validator;

    @BeforeAll
    public static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validPasswordTest() {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setPassword("password123456");

        Set<ConstraintViolation<ResetPasswordRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    public void blankPasswordTest() {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setPassword("");

        Set<ConstraintViolation<ResetPasswordRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("must not be blank");
    }

    @Test
    public void passwordExceedsMaxLengthTest() {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setPassword("a".repeat(65));

        Set<ConstraintViolation<ResetPasswordRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("size must be between 0 and 64");
    }

}

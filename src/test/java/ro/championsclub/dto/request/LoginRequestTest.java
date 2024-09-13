package ro.championsclub.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class LoginRequestTest {

    private static Validator validator;

    @BeforeAll
    public static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validLoginRequestTest() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@email.com");
        request.setPassword("password123456");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(0);
    }

    @Test
    public void blankEmailAndPasswordTest() {
        LoginRequest request = new LoginRequest();
        request.setEmail("");
        request.setPassword("");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(2);
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("must not be blank");
    }

    @Test
    public void emailAndPasswordExceedsMaxLengthTest() {
        LoginRequest request = new LoginRequest();
        request.setEmail("a".repeat(65) + "@email.com");
        request.setPassword("p".repeat(65));

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(2);
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("size must be between 0 and 64");
    }

}

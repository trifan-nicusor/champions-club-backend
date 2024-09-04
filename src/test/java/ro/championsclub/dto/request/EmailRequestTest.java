package ro.championsclub.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class EmailRequestTest {

    private static Validator validator;

    @BeforeAll
    public static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validEmailTest() {
        EmailRequest request = new EmailRequest();
        request.setEmail("test@email.com");

        Set<ConstraintViolation<EmailRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    public void blankEmailTest() {
        EmailRequest request = new EmailRequest();
        request.setEmail("");

        Set<ConstraintViolation<EmailRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("must not be blank");
    }

    @Test
    public void emailExceedsMaxLengthTest() {
        EmailRequest request = new EmailRequest();
        request.setEmail("a".repeat(65) + "@email.com");

        Set<ConstraintViolation<EmailRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("size must be between 0 and 64");
    }

}

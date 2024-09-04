package ro.championsclub.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RegisterRequestTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validRegisterRequestTest() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("test@email.com");
        request.setPassword("password123456");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(0);
    }

    @Test
    public void blankFirstNameTest() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("");
        request.setLastName("Doe");
        request.setEmail("test@email.com");
        request.setPassword("password123456");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("must not be blank");
        assertThat(violations).extracting(violation -> violation.getPropertyPath().toString()).contains("firstName");
    }

    @Test
    public void firstNameExceedsMaxLengthTest() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("a".repeat(33));
        request.setLastName("Doe");
        request.setEmail("test@email.com");
        request.setPassword("password123456");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("size must be between 0 and 32");
        assertThat(violations).extracting(violation -> violation.getPropertyPath().toString()).contains("firstName");
    }

    @Test
    public void blankLastNameTest() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("John");
        request.setLastName("");
        request.setEmail("test@email.com");
        request.setPassword("password123456");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("must not be blank");
        assertThat(violations).extracting(violation -> violation.getPropertyPath().toString()).contains("lastName");
    }

    @Test
    public void lastNameExceedsMaxLengthTest() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("John");
        request.setLastName("a".repeat(33));
        request.setEmail("test@example.com");
        request.setPassword("password123456");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("size must be between 0 and 32");
        assertThat(violations).extracting(violation -> violation.getPropertyPath().toString()).contains("lastName");
    }

    @Test
    public void invalidEmailFormatTest() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("invalid-email-format");
        request.setPassword("password123456");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("invalid email format");
        assertThat(violations).extracting(violation -> violation.getPropertyPath().toString()).contains("email");
    }

    @Test
    public void blankEmailTest() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("");
        request.setPassword("password123456");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(2);
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("must not be blank");
        assertThat(violations).extracting(violation -> violation.getPropertyPath().toString()).contains("email");
    }

    @Test
    public void emailExceedsMaxLengthTest() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("a".repeat(65) + "@email.com");
        request.setPassword("password123456");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(2);
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("size must be between 0 and 64");
        assertThat(violations).extracting(violation -> violation.getPropertyPath().toString()).contains("email");
    }

    @Test
    public void blankPasswordTest() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("test@email.com");
        request.setPassword("");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("must not be blank");
        assertThat(violations).extracting(violation -> violation.getPropertyPath().toString()).contains("password");
    }

    @Test
    public void passwordExceedsMaxLengthTest() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("test@email.com");
        request.setPassword("p".repeat(65));

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("size must be between 0 and 64");
        assertThat(violations).extracting(violation -> violation.getPropertyPath().toString()).contains("password");
    }

}

package ro.championsclub.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ro.championsclub.dto.ValidationDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConstraintValidatorTest {

    private ConstraintValidator constraintValidator;

    @Mock
    private BindingResult mockBindingResult;

    @BeforeEach
    public void setup() {
        constraintValidator = new ConstraintValidator();
    }

    @Test
    public void buildErrorsTest() {
        var fieldError = new FieldError("name", "field", "must not be blank");

        when(mockBindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        List<ValidationDto> result = constraintValidator.buildErrors(mockBindingResult);

        assertThat(result).hasSize(1);

        ValidationDto validationDto = result.getFirst();

        assertThat("field").isEqualTo(validationDto.field());
        assertThat("must not be blank").isEqualTo(validationDto.message());
        assertThat(validationDto.timestamp()).isNotNull();
    }

}

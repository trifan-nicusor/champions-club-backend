package ro.championsclub.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ro.championsclub.component.ConstraintValidator;
import ro.championsclub.dto.ErrorDto;
import ro.championsclub.dto.ValidationDto;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ControllerAdviceTest {

    @Mock
    private ConstraintValidator constraintValidator;

    @InjectMocks
    private ControllerAdvice controllerAdvice;

    @Test
    void handleEntityNotFoundException() {
        String message = "Entity not found";
        EntityNotFoundException exception = new EntityNotFoundException(message);

        ErrorDto response = controllerAdvice.handle(exception);

        assertEquals(message, response.message());
        assertNotNull(response.timestamp());
    }

    @Test
    void handleResourceConflictException() {
        String message = "Resource conflict";
        ResourceConflictException exception = new ResourceConflictException(message);

        ErrorDto response = controllerAdvice.handle(exception);

        assertEquals(message, response.message());
        assertNotNull(response.timestamp());
    }

    @Test
    void handleTechnicalAndBusinessExceptions() {
        String message = "Technical error";
        TechnicalException exception = new TechnicalException(message);

        ErrorDto response = controllerAdvice.handle(exception);

        assertEquals(message, response.message());
        assertNotNull(response.timestamp());

        message = "Business error";
        BusinessException businessException = new BusinessException(message);

        response = controllerAdvice.handle(businessException);

        assertEquals(message, response.message());
        assertNotNull(response.timestamp());
    }

    @Test
    void handleMethodArgumentNotValidException() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        ValidationDto validationDto = new ValidationDto("field", "error message");
        List<ValidationDto> validationErrors = List.of(validationDto);
        when(constraintValidator.buildErrors(bindingResult)).thenReturn(validationErrors);

        List<ValidationDto> response = controllerAdvice.handle(exception);

        assertEquals(1, response.size());
        assertEquals("field", response.getFirst().field());
        assertEquals("error message", response.getFirst().error());
        assertNotNull(response.getFirst().timestamp());
        verify(constraintValidator).buildErrors(bindingResult);
    }

    @Test
    void handleConstraintViolationException() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<?>> violations = Set.of(violation);
        ConstraintViolationException exception = new ConstraintViolationException(violations);

        ValidationDto validationDto = new ValidationDto("field", "error message");
        List<ValidationDto> validationErrors = List.of(validationDto);
        when(constraintValidator.buildErrors(violations)).thenReturn(validationErrors);

        List<ValidationDto> response = controllerAdvice.handle(exception);

        assertEquals(1, response.size());
        assertEquals("field", response.getFirst().field());
        assertEquals("error message", response.getFirst().error());
        assertNotNull(response.getFirst().timestamp());
        verify(constraintValidator).buildErrors(violations);
    }

}

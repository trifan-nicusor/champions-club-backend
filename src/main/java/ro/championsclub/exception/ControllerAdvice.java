package ro.championsclub.exception;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ro.championsclub.component.ConstraintValidator;
import ro.championsclub.dto.ErrorDto;
import ro.championsclub.dto.ValidationDto;

import java.util.List;

@Slf4j
@Hidden
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {

    private final ConstraintValidator constraintValidator;

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handle(EntityNotFoundException e) {
        return new ErrorDto(e.getMessage());
    }

    @ExceptionHandler(ResourceConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handle(ResourceConflictException e) {
        log.error("resource conflict: {}", e.getMessage());

        return new ErrorDto(e.getMessage());
    }

    @ExceptionHandler({
            TechnicalException.class,
            BusinessException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handle(RuntimeException e) {
        log.error("bad request: {}", e.getMessage());

        return new ErrorDto(e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ValidationDto> handle(MethodArgumentNotValidException e) {
        log.error("invalid method arguments: {}", e.getMessage());

        return constraintValidator.buildErrors(e.getBindingResult());
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(ConstraintViolationException.class)
    public List<ValidationDto> handle(ConstraintViolationException e) {
        log.error("constraint validation failed: {}", e.getMessage());

        return constraintValidator.buildErrors(e.getConstraintViolations());
    }

}

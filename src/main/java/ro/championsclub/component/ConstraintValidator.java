package ro.championsclub.component;

import jakarta.validation.ConstraintViolation;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import ro.championsclub.dto.ValidationDto;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class ConstraintValidator {

    public List<ValidationDto> buildErrors(Set<ConstraintViolation<?>> violations) {
        return violations.stream()
                .map(violation -> new ValidationDto(
                        Objects.requireNonNull(
                                StreamSupport.stream(violation.getPropertyPath().spliterator(), false)
                                        .reduce((_, second) -> second)
                                        .orElse(null)
                        ).toString(),
                        violation.getMessage()
                )).collect(Collectors.toList());
    }

    public List<ValidationDto> buildErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream()
                .map(fieldError ->
                        new ValidationDto(
                                fieldError.getField(),
                                fieldError.getDefaultMessage()
                        )
                ).collect(Collectors.toList());
    }

}

package ro.championsclub.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ro.championsclub.constant.EmailRegex;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank
    @Size(max = 32)
    private String firstName;

    @NotBlank
    @Size(max = 32)
    private String lastName;

    @Email(
            regexp = EmailRegex.EXPRESSION,
            message = "invalid email format"
    )
    @NotBlank
    @Size(max = 64)
    private String email;

    @NotBlank
    @Size(max = 64)
    private String password;

}
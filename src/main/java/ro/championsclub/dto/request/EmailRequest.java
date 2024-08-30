package ro.championsclub.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequest {

    @NotBlank
    @Size(max = 64)
    private String email;

}

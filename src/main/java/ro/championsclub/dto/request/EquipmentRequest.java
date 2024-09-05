package ro.championsclub.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentRequest {

    @NotBlank
    @Size(max = 32)
    private String name;

    @NotBlank
    @Size(max = 32)
    private String category;

}

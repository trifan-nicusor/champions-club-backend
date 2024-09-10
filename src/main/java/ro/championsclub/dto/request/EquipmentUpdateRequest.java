package ro.championsclub.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ro.championsclub.constant.EquipmentCategoryEnum;

@Getter
@Setter
public final class EquipmentUpdateRequest {

    @Size(max = 32)
    private String name;

    @NotNull
    private EquipmentCategoryEnum category;

}

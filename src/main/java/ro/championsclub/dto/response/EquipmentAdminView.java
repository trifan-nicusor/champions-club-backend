package ro.championsclub.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ro.championsclub.constant.EquipmentCategoryEnum;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class EquipmentAdminView {

    private int id;
    private String equipmentName;
    private EquipmentCategoryEnum category;
    private LocalDateTime createAt;
    private String imageData;
    private String imageName;

}

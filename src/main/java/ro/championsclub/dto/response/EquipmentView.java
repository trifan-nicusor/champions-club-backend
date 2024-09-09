package ro.championsclub.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ro.championsclub.constant.EquipmentCategoryEnum;

@Getter
@Setter
@NoArgsConstructor
public class EquipmentView {

    private String equipmentName;
    private String imageName;
    private String imageData;
    private EquipmentCategoryEnum category;

}

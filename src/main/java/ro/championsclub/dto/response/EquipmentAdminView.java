package ro.championsclub.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EquipmentAdminView extends EquipmentView {

    private int id;
    private LocalDateTime createAt;

}

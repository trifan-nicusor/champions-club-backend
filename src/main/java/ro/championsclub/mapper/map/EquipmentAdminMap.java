package ro.championsclub.mapper.map;

import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
import ro.championsclub.dto.response.EquipmentAdminView;
import ro.championsclub.entity.Equipment;

import java.util.Base64;

public class EquipmentAdminMap extends PropertyMap<Equipment, EquipmentAdminView> {

    @Override
    protected void configure() {
        Converter<byte[], String> toBase64 = context -> Base64.getEncoder().encodeToString(context.getSource());

        map().setId(source.getId());
        map().setEquipmentName(source.getName());
        map().setCategory(source.getCategory());
        map().setCreateAt(source.getCreatedAt());
        using(toBase64).map(source.getImage().getData()).setImageData(null);
        map().setImageName(source.getImage().getName());
    }

}

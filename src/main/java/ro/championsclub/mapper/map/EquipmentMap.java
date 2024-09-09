package ro.championsclub.mapper.map;

import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
import ro.championsclub.dto.response.EquipmentView;
import ro.championsclub.entity.Equipment;

import java.util.Base64;

public class EquipmentMap extends PropertyMap<Equipment, EquipmentView> {

    @Override
    protected void configure() {
        Converter<byte[], String> toBase64 = context -> Base64.getEncoder().encodeToString(context.getSource());

        map().setEquipmentName(source.getName());
        map().setImageName(source.getImage().getName());
        using(toBase64).map(source.getImage().getData()).setImageData(null);
        map().setCategory(source.getCategory());
    }

}

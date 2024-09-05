package ro.championsclub.mapper;

import org.modelmapper.Conditions;
import org.modelmapper.convention.MatchingStrategies;
import ro.championsclub.mapper.map.EquipmentAdminMap;
import ro.championsclub.mapper.map.EquipmentMap;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ModelMapper {

    private static final org.modelmapper.ModelMapper modelMapper;

    static {
        modelMapper = new org.modelmapper.ModelMapper();

        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setPropertyCondition(Conditions.isNotNull())
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        modelMapper.addMappings(new EquipmentAdminMap());
        modelMapper.addMappings(new EquipmentMap());
    }

    private ModelMapper() {

    }

    public static <S, D> void map(final S source, D target) {
        modelMapper.map(source, target);
    }

    public static <D, T> D map(final T source, Class<D> targetClass) {
        return modelMapper.map(source, targetClass);
    }

    public static <D, T> List<D> mapAll(final Collection<T> sources, Class<D> target) {

        return sources.stream()
                .map(entity -> map(entity, target))
                .collect(Collectors.toList());
    }

}

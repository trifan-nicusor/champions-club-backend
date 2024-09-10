package ro.championsclub.mapper.map;

import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
import ro.championsclub.dto.response.SubscriptionAdminView;
import ro.championsclub.entity.Subscription;

import java.util.Base64;

public class SubscriptionAdminMap extends PropertyMap<Subscription, SubscriptionAdminView> {

    @Override
    protected void configure() {
        Converter<byte[], String> toBase64 = context -> Base64.getEncoder().encodeToString(context.getSource());

        map().setId(source.getId());
        map().setSubscriptionName(source.getName());
        map().setPrice(source.getPrice());
        map().setDurationInMonths(source.getDurationInMonths());
        map().setStartingHour(source.getStartingHour());
        map().setEndingHour(source.getEndingHour());
        map().setCreatedAt(source.getCreatedAt());
        map().setImageName(source.getImage().getName());
        using(toBase64).map(source.getImage().getData()).setImageData(null);
    }

}

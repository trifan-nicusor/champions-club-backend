package ro.championsclub.mapper.map;

import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
import ro.championsclub.dto.response.SubscriptionView;
import ro.championsclub.entity.Subscription;

import java.util.Base64;

public class SubscriptionMap extends PropertyMap<Subscription, SubscriptionView> {

    @Override
    protected void configure() {
        Converter<byte[], String> toBase64 = context -> Base64.getEncoder().encodeToString(context.getSource());

        map().setSubscriptionName(source.getName());
        map().setPrice(source.getPrice());
        map().setDurationInMonths(source.getDurationInMonths());
        map().setStartingHour(source.getStartingHour());
        map().setEndingHour(source.getEndingHour());
        map().setImageName(source.getImage().getName());
        using(toBase64).map(source.getImage().getData()).setImageData(null);
    }

}

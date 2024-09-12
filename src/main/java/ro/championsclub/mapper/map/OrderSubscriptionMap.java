package ro.championsclub.mapper.map;

import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
import ro.championsclub.entity.OrderSubscriptions;
import ro.championsclub.entity.Subscription;

import java.time.LocalDate;

public class OrderSubscriptionMap extends PropertyMap<Subscription, OrderSubscriptions> {

    @Override
    protected void configure() {
        Converter<Subscription, LocalDate> validToConverter = context -> LocalDate.now().plusDays(context.getSource().getDurationInMonths() * 30);

        map().setName(source.getName());
        map().setPrice(source.getPrice());
        using(validToConverter).map(source).setValidTo(null);
        map().setStartingHour(source.getStartingHour());
        map().setEndingHour(source.getEndingHour());
        map().setDurationInMonths(source.getDurationInMonths());
    }

}

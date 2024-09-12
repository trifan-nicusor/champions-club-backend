package ro.championsclub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ro.championsclub.dto.request.SubscriptionRequest;
import ro.championsclub.dto.request.SubscriptionUpdateRequest;
import ro.championsclub.dto.response.SubscriptionAdminView;
import ro.championsclub.dto.response.SubscriptionView;
import ro.championsclub.entity.Subscription;
import ro.championsclub.exception.ResourceConflictException;
import ro.championsclub.mapper.ModelMapper;
import ro.championsclub.repository.SubscriptionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final ImageService imageService;

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public void saveSubscription(MultipartFile file, SubscriptionRequest request) {
        String name = request.getName();

        if (subscriptionRepository.existsByName(name)) {
            throw new ResourceConflictException("Subscription with name: " + name + " already exists");
        }

        var image = imageService.saveImage(file);

        var subscription = Subscription.builder()
                .name(name)
                .price(request.getPrice())
                .durationInMonths(request.getDurationInMonths())
                .startingHour(request.getStartingHour())
                .endingHour(request.getEndingHour())
                .image(image)
                .build();

        subscriptionRepository.save(subscription);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteSubscription(String name) {
        subscriptionRepository.disable(name);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void updateSubscription(
            String name,
            MultipartFile file,
            SubscriptionUpdateRequest request
    ) {
        if (name.equals(request.getName())) {
            throw new ResourceConflictException("Subscription with name: " + name + " already exists");
        }

        var subscription = subscriptionRepository.getByName(name);

        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();

            if (subscription.getImage().getName().equals(fileName)) {
                throw new ResourceConflictException("Image with name: " + fileName + " already exists");
            }

            var image = imageService.saveImage(file);

            subscription.setImage(image);
        }

        ModelMapper.map(request, subscription);

        subscriptionRepository.save(subscription);
    }

    public List<SubscriptionView> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionRepository.findAllByIsActiveTrue();

        return ModelMapper.mapAll(subscriptions, SubscriptionView.class);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public List<SubscriptionAdminView> getAllActiveSubscriptions() {
        List<Subscription> subscriptions = subscriptionRepository.findAllByIsActiveTrue();

        return ModelMapper.mapAll(subscriptions, SubscriptionAdminView.class);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public List<SubscriptionAdminView> getAllInactiveSubscriptions() {
        List<Subscription> subscriptions = subscriptionRepository.findAllByIsActiveFalse();

        return ModelMapper.mapAll(subscriptions, SubscriptionAdminView.class);
    }

}

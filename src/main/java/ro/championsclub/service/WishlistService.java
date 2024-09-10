package ro.championsclub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ro.championsclub.dto.response.WishlistView;
import ro.championsclub.entity.User;
import ro.championsclub.entity.Wishlist;
import ro.championsclub.mapper.ModelMapper;
import ro.championsclub.repository.SubscriptionRepository;
import ro.championsclub.repository.WishlistRepository;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final SubscriptionRepository subscriptionRepository;

    @PreAuthorize("hasAuthority('USER')")
    public WishlistView getMyWishlist(User user) {
        var wishlist = user.getWishlist();

        if (wishlist == null) {
            wishlist = buildAndSaveWishlist(user);
        }

        return ModelMapper.map(wishlist, WishlistView.class);
    }

    @PreAuthorize("hasAuthority('USER')")
    public void addSubscription(User user, String subName) {
        var wishlist = user.getWishlist();
        var subscription = subscriptionRepository.getByName(subName);

        if (wishlist == null) {
            wishlist = buildAndSaveWishlist(user);
        }

        wishlist.getSubscriptions().add(subscription);
        wishlistRepository.save(wishlist);
    }


    @PreAuthorize("hasAuthority('USER')")
    public void removeSubscriptions(User user, String subName) {
        var wishlist = user.getWishlist();

        if (wishlist == null) {
            return;
        }

        var subscription = subscriptionRepository.getByName(subName);

        wishlist.getSubscriptions().remove(subscription);
        wishlistRepository.save(wishlist);
    }

    private Wishlist buildAndSaveWishlist(User user) {
        var wishlist = Wishlist.builder()
                .user(user)
                .build();

        return wishlistRepository.save(wishlist);
    }

}

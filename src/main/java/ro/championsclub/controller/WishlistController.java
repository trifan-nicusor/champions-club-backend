package ro.championsclub.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ro.championsclub.dto.response.WishlistView;
import ro.championsclub.entity.User;
import ro.championsclub.service.WishlistService;

@RestController
@RequestMapping("/api/v1/wishlist")
@Validated
@RequiredArgsConstructor
@Tag(name = "Wishlist Controller")
public class WishlistController {

    private final WishlistService service;

    @Operation(summary = "[only for users] get my wishlist")
    @GetMapping
    public WishlistView getMyWishlist(@AuthenticationPrincipal User user) {
        return service.getMyWishlist(user);
    }

    @Operation(summary = "[only for users] add subscription to wishlist")
    @PostMapping("/add-subscription/{subName}")
    public void addSubscriptionToWishlist(
            @AuthenticationPrincipal User user,
            @PathVariable String subName
    ) {
        service.addSubscription(user, subName);
    }

    @Operation(summary = "[only for users] remove subscription from wishlist")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/remove-subscription/{subName}")
    public void removeSubscriptionFromWishlist(
            @AuthenticationPrincipal User user,
            @PathVariable String subName
    ) {
        service.removeSubscriptions(user, subName);
    }

}

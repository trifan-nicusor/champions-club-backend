package ro.championsclub.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ro.championsclub.dto.response.CartView;
import ro.championsclub.entity.User;
import ro.championsclub.service.CartService;

@RestController
@RequestMapping("/api/v1/cart")
@Validated
@RequiredArgsConstructor
@Tag(name = "Cart Controller")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "[only for users] get my cart")
    @ApiResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = CartView.class))
    )
    @GetMapping
    public CartView getMyCart(@AuthenticationPrincipal User user) {
        return cartService.getMyCart(user);
    }

    @Operation(summary = "[only for users] add subscription to cart")
    @ApiResponse(responseCode = "200")
    @PostMapping("/add-subscription/{subName}")
    public void addSubscriptionToCart(
            @AuthenticationPrincipal User user,
            @PathVariable String subName
    ) {
        cartService.addSubscription(user, subName);
    }

    @Operation(summary = "[only for users] remove subscription from cart")
    @ApiResponse(responseCode = "200")
    @GetMapping("/remove-subscription/{subName}")
    public CartView removeSubscriptionFromCart(
            @AuthenticationPrincipal User user,
            @PathVariable String subName
    ) {
        return cartService.removeSubscription(user, subName);
    }

    @Operation(summary = "[only for admins and users] remove discount from cart")
    @DeleteMapping("/remove-discount/{discCode}")
    public CartView removeDiscountFromCart(
            @AuthenticationPrincipal User user,
            @PathVariable String discCode
    ) {
        return cartService.removeDiscount(user, discCode);
    }

    @Operation(summary = "[only for admins and users] add discount to cart")
    @PutMapping("/add-discount/{discCode}")
    public CartView addDiscountToCart(
            @AuthenticationPrincipal User user,
            @PathVariable String discCode
    ) {
        return cartService.addDiscount(user, discCode);
    }

}

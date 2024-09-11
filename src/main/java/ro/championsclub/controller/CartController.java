package ro.championsclub.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ro.championsclub.dto.ErrorDto;
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
    @GetMapping
    public CartView getMyCart(@AuthenticationPrincipal User user) {
        return cartService.getMyCart(user);
    }

    @Operation(summary = "[only for users] add subscription to cart")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Subscription not found",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))
            )
    })
    @PutMapping("/add-subscription/{name}")
    public void addSubscriptionToCart(
            @AuthenticationPrincipal User user,
            @PathVariable String name
    ) {
        cartService.addSubscription(user, name);
    }

    @Operation(summary = "[only for users] remove subscription from cart")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Subscription not found",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))
            )
    })
    @DeleteMapping("/remove-subscription/{name}")
    public CartView removeSubscriptionFromCart(
            @AuthenticationPrincipal User user,
            @PathVariable String name
    ) {
        return cartService.removeSubscription(user, name);
    }

    @Operation(summary = "[only for users] add discount to cart")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Discount not found",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))
            )
    })
    @PutMapping("/add-discount/{code}")
    public CartView addDiscountToCart(
            @AuthenticationPrincipal User user,
            @PathVariable String code
    ) {
        return cartService.addDiscount(user, code);
    }

    @Operation(summary = "[only for users] remove discount from cart")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Discount not found",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))
            )
    })
    @DeleteMapping("/remove-discount/{code}")
    public CartView removeDiscountFromCart(
            @AuthenticationPrincipal User user,
            @PathVariable String code
    ) {
        return cartService.removeDiscount(user, code);
    }

}

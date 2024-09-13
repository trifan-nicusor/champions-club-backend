package ro.championsclub.controller;

import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ro.championsclub.dto.ErrorDto;
import ro.championsclub.entity.User;
import ro.championsclub.service.PaymentService;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Tag(name = "Payment Controller")
public class PaymentController {

    private final PaymentService service;

    @Operation(summary = "[only for users] get my orders")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Cart must contain at least one subscription",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))
            )
    })
    @GetMapping("/generate-link")
    public String generatePaymentLink(@AuthenticationPrincipal User user) throws StripeException {
        return service.generatePaymentLink(user);
    }

    @Operation(summary = "handle webhooks from stripe")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Failed to handle webhooks",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))
            )
    })
    @PostMapping("/success")
    public void handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String header
    ) {
        service.handleSuccessfulPayment(payload, header);
    }

}

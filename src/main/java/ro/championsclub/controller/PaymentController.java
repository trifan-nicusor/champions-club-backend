package ro.championsclub.controller;

import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ro.championsclub.entity.User;
import ro.championsclub.service.PaymentService;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Tag(name = "Payment Controller")
public class PaymentController {

    private final PaymentService service;

    @GetMapping("/generate-link")
    public String generatePaymentLink(@AuthenticationPrincipal User user) throws StripeException {
        return service.generatePaymentLink(user);
    }

    @PostMapping("/success")
    public void handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String header
    ) {
        service.handleSuccessfulPayment(payload, header);
    }

}

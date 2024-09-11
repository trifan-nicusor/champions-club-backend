package ro.championsclub.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ro.championsclub.entity.Cart;
import ro.championsclub.entity.Subscription;
import ro.championsclub.entity.User;
import ro.championsclub.exception.BusinessException;
import ro.championsclub.exception.TechnicalException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final CartService cartService;
    private final OrderService orderService;

    @Value("${stripe.key.secret}")
    private String STRIPE_SECRET_KEY;

    @Value("${stripe.webhook.secret}")
    private String ENDPOINT_SECRET;


    @PreAuthorize("hasAuthority('USER')")
    public String generatePaymentLink(User user) throws StripeException {
        Stripe.apiKey = STRIPE_SECRET_KEY;
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
        Cart cart = cartService.getCartByUser(user);

        if (cart.getProducts().isEmpty()) {
            throw new BusinessException("Cart must contain at least one product");
        }

        var oneHundred = BigDecimal.valueOf(100);
        var discount = cart.getDiscount().divide(BigDecimal.valueOf(cart.getProducts().size()), RoundingMode.CEILING);

        cart.getProducts().forEach(product -> {
                    Subscription subscription = product.getSubscription();

                    lineItems.add(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("ron")
                                                    .setUnitAmountDecimal(subscription.getPrice().subtract(discount).multiply(oneHundred))
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName(subscription.getName())
                                                                    .build()
                                                    ).build()
                                    ).build());
                }
        );

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setPaymentIntentData(
                        SessionCreateParams.PaymentIntentData.builder()
                                .putMetadata("email", user.getEmail())
                                .build()
                )
                .addAllLineItem(lineItems)
                .build();

        return Session.create(params).getUrl();
    }

    public void handleSuccessfulPayment(String payload, String header) {
        try {
            Event event = Webhook.constructEvent(payload, header, ENDPOINT_SECRET);
            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
            StripeObject stripeObject = null;

            if (dataObjectDeserializer.getObject().isPresent()) {
                stripeObject = dataObjectDeserializer.getObject().get();

                log.info("Stripe object is present");
            }

            if (event.getType().equals("payment_intent.succeeded")) {
                var paymentIntent = (PaymentIntent) stripeObject;

                String email = paymentIntent.getMetadata().get("email");

                orderService.createOrder(email);

                log.info("Payment successfully");
            }

            log.info("Unhandled event: {}", event.getType());
        } catch (Exception e) {
            throw new TechnicalException("Webhook failed" + e.getMessage());
        }
    }

}

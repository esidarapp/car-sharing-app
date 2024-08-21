package com.product.carsharing.service.impl.stripe;

import com.product.carsharing.exception.DataProcessingException;
import com.product.carsharing.service.stripe.StripeService;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class StripeServiceImpl implements StripeService {
    @Value("${stripe.api.key}")
    private String apiKey;

    @Value("${stripe.success.url}")
    private String successUrl;

    @Value("${stripe.cancel.url}")
    private String cancelUrl;

    @Override
    public Session createSession(BigDecimal amount) {
        Stripe.apiKey = apiKey;
        try {
            String successUrl = UriComponentsBuilder.fromHttpUrl(this.successUrl)
                    .queryParam("sessionId", "{CHECKOUT_SESSION_ID}")
                    .build().toUriString();
            String cancelUrl = UriComponentsBuilder.fromHttpUrl(this.cancelUrl)
                    .queryParam("sessionId", "{CHECKOUT_SESSION_ID}")
                    .build().toUriString();
            return Session.create(createSessionParams(amount, successUrl, cancelUrl));
        } catch (Exception e) {
            throw new DataProcessingException("Failed to create Stripe session", e);
        }
    }

    private SessionCreateParams createSessionParams(BigDecimal amount,
                                                    String successUrl,
                                                    String cancelUrl) {
        return SessionCreateParams.builder()
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(amount.multiply(BigDecimal.valueOf(100)).longValue())
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Car Rental").build())
                                .build())
                        .setQuantity(1L).build())
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl).build();
    }
}

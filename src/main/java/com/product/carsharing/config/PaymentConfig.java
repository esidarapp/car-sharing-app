package com.product.carsharing.config;

import com.product.carsharing.model.Payment;
import com.product.carsharing.service.impl.stripe.FineAmountHandler;
import com.product.carsharing.service.impl.stripe.PaymentAmountHandler;
import com.product.carsharing.service.stripe.AmountHandler;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {
    @Autowired
    private PaymentAmountHandler paymentAmountHandler;
    @Autowired
    private FineAmountHandler fineAmountHandler;

    @Bean
    public HashMap<Payment.Type, AmountHandler> initAmountHandlers() {
        HashMap<Payment.Type, AmountHandler> handlers = new HashMap<>();
        handlers.put(Payment.Type.PAYMENT, paymentAmountHandler);
        handlers.put(Payment.Type.FINE, fineAmountHandler);
        return handlers;
    }
}

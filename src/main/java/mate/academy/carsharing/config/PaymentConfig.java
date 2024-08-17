package mate.academy.carsharing.config;

import java.util.HashMap;
import mate.academy.carsharing.model.Payment;
import mate.academy.carsharing.service.impl.stripe.FineAmountHandler;
import mate.academy.carsharing.service.impl.stripe.PaymentAmountHandler;
import mate.academy.carsharing.service.stripe.AmountHandler;
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

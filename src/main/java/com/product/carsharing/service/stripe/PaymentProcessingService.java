package com.product.carsharing.service.stripe;

import com.product.carsharing.model.Payment;
import com.product.carsharing.model.Rental;
import java.math.BigDecimal;

public interface PaymentProcessingService {
    void handlePaymentSuccess(String sessionId);

    void handlePaymentCancel(String sessionId);

    BigDecimal getAmount(Rental rental, Payment.Type type);
}

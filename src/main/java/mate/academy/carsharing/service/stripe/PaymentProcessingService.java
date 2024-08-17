package mate.academy.carsharing.service.stripe;

import java.math.BigDecimal;
import mate.academy.carsharing.model.Payment;
import mate.academy.carsharing.model.Rental;

public interface PaymentProcessingService {
    void handlePaymentSuccess(String sessionId);

    void handlePaymentCancel(String sessionId);

    BigDecimal getAmount(Rental rental, Payment.Type type);
}

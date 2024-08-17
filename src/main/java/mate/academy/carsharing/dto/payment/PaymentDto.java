package mate.academy.carsharing.dto.payment;

import java.math.BigDecimal;
import mate.academy.carsharing.model.Payment;

public record PaymentDto(
        Long id,
        Payment.Status status,
        Payment.Type type,
        Long rentalId,
        String sessionUrl,
        String sessionId,
        BigDecimal amount
) {
}

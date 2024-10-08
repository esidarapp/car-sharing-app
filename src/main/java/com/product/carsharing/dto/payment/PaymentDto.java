package com.product.carsharing.dto.payment;

import com.product.carsharing.model.Payment;
import java.math.BigDecimal;

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

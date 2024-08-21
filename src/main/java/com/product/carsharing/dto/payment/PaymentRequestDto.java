package com.product.carsharing.dto.payment;

import com.product.carsharing.model.Payment;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PaymentRequestDto(
        @NotNull
        @Min(1)
        Long rentalId,
        @NotNull
        Payment.Type type
) {
}

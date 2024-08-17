package mate.academy.carsharing.dto.payment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import mate.academy.carsharing.model.Payment;

public record PaymentRequestDto(
        @NotNull
        @Min(1)
        Long rentalId,
        @NotNull
        Payment.Type type
) {
}

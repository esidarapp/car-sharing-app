package com.product.carsharing.dto.rental;

import java.time.LocalDate;

public record RentalDto(
        Long id,
        LocalDate rentalDate,
        LocalDate returnDate,
        Long carId,
        Long userId
) {
}

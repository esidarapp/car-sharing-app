package com.product.carsharing.dto.rental;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateRentalDto(
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate returnDate,
        @NotNull
        Long carId
) {
}

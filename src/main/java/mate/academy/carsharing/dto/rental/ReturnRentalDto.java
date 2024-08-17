package mate.academy.carsharing.dto.rental;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReturnRentalDto(
        @NotNull
        Long rentalId,
        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate actualReturnDate
) {
}

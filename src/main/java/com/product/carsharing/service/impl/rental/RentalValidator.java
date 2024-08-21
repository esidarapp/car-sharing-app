package com.product.carsharing.service.impl.rental;

import com.product.carsharing.exception.DataProcessingException;
import com.product.carsharing.model.Rental;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Service;

@Service
public class RentalValidator {
    public static final String RETURN_DATE_VALIDATION =
            "Return date must be at least one day after the rental date.";

    public void validateRentalDates(Rental rental) {
        LocalDate rentalDate = rental.getRentalDate();
        LocalDate returnDate = rental.getReturnDate();

        long daysBetween = ChronoUnit.DAYS.between(rentalDate, returnDate);
        if (daysBetween < 1) {
            throw new DataProcessingException(RETURN_DATE_VALIDATION);
        }
    }
}

package mate.academy.carsharing.service.impl.stripe;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import mate.academy.carsharing.model.Rental;
import mate.academy.carsharing.service.stripe.AmountHandler;
import org.springframework.stereotype.Component;

@Component
public class PaymentAmountHandler implements AmountHandler {
    @Override
    public BigDecimal getAmount(Rental rental) {
        long daysCount = ChronoUnit.DAYS.between(rental.getRentalDate(), rental.getReturnDate());
        BigDecimal dailyFee = rental.getCar().getDailyFee();
        return dailyFee.multiply(BigDecimal.valueOf(daysCount));
    }
}

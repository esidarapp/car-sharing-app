package com.product.carsharing.service.impl.stripe;

import com.product.carsharing.model.Rental;
import com.product.carsharing.service.stripe.AmountHandler;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
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

package com.product.carsharing.service.impl.stripe;

import com.product.carsharing.model.Rental;
import com.product.carsharing.service.stripe.AmountHandler;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FineAmountHandler implements AmountHandler {
    @Value("${payment.fine.multiplier}")
    private BigDecimal fineMultiplier;

    @Override
    public BigDecimal getAmount(Rental rental) {
        long daysCount = ChronoUnit.DAYS.between(
                rental.getReturnDate(), rental.getActualReturnDate());
        BigDecimal dailyFee = rental.getCar().getDailyFee();
        return dailyFee.multiply(BigDecimal.valueOf(daysCount)).multiply(fineMultiplier);
    }
}

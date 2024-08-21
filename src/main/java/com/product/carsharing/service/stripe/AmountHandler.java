package com.product.carsharing.service.stripe;

import com.product.carsharing.model.Rental;
import java.math.BigDecimal;

public interface AmountHandler {
    BigDecimal getAmount(Rental rental);
}

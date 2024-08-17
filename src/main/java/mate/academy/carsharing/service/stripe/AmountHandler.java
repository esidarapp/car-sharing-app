package mate.academy.carsharing.service.stripe;

import java.math.BigDecimal;
import mate.academy.carsharing.model.Rental;

public interface AmountHandler {
    BigDecimal getAmount(Rental rental);
}

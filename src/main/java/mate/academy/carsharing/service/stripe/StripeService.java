package mate.academy.carsharing.service.stripe;

import com.stripe.model.checkout.Session;
import java.math.BigDecimal;

public interface StripeService {
    Session createSession(BigDecimal amount);
}

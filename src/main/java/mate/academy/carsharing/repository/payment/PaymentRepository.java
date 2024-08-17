package mate.academy.carsharing.repository.payment;

import java.util.List;
import java.util.Optional;
import mate.academy.carsharing.model.Payment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findBySessionId(String sessionId);

    @EntityGraph(value = "Rental.details")
    List<Payment> findByRental_User_Id(Long userId);
}

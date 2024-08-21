package com.product.carsharing.repository.payment;

import com.product.carsharing.model.Payment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findBySessionId(String sessionId);

    @EntityGraph(value = "Rental.details")
    List<Payment> findByRental_User_Id(Long userId);
}

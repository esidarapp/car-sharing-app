package com.product.carsharing.repository.rental;

import com.product.carsharing.model.Rental;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    @EntityGraph(value = "Rental.details")
    Optional<Rental> findById(Long rentalId);

    @EntityGraph(value = "Rental.details")
    List<Rental> findAll(Specification<Rental> spec);
}

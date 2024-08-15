package mate.academy.carsharing.repository.rental;

import java.util.List;
import java.util.Optional;
import mate.academy.carsharing.model.Rental;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    @EntityGraph(attributePaths = {"car", "user"})
    Optional<Rental> findById(Long rentalId);

    List<Rental> findAll(Specification<Rental> spec);
}

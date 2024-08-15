package mate.academy.carsharing.repository.rental.filter;

import mate.academy.carsharing.dto.rental.RentalSearchParams;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(RentalSearchParams searchParams);
}

package mate.academy.carsharing.repository.rental.filter.providers;

import mate.academy.carsharing.model.Rental;
import mate.academy.carsharing.repository.rental.filter.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class UserIdSpecificationProvider implements SpecificationProvider<Rental> {
    private static final String FIELD_USER_ID = "user";

    @Override
    public String getKey() {
        return FIELD_USER_ID;
    }

    @Override
    public Specification<Rental> getSpecification(String param) {
        return (root, query, criteriaBuilder) -> root.get(FIELD_USER_ID).get("id").in(param);
    }
}

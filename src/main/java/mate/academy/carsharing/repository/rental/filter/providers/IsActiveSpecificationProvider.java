package mate.academy.carsharing.repository.rental.filter.providers;

import mate.academy.carsharing.model.Rental;
import mate.academy.carsharing.repository.rental.filter.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class IsActiveSpecificationProvider implements SpecificationProvider<Rental> {
    private static final String FIELD_IS_ACTIVE = "is_active";
    private static final String FIELD_ACTUAL_RETURN_DATE = "actualReturnDate";

    @Override
    public String getKey() {
        return FIELD_IS_ACTIVE;
    }

    //if "actualReturnDate" field is null - means that rental is still active
    @Override
    public Specification<Rental> getSpecification(String param) {
        return (root, query, criteriaBuilder) -> {
            if (Boolean.parseBoolean(param)) {
                return criteriaBuilder.isNull(root.get(FIELD_ACTUAL_RETURN_DATE));
            } else {
                return criteriaBuilder.isNotNull(root.get(FIELD_ACTUAL_RETURN_DATE));
            }
        };
    }
}

package com.product.carsharing.repository.rental.filter;

import com.product.carsharing.dto.rental.RentalSearchParams;
import com.product.carsharing.model.Rental;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RentalSpecificationBuilder implements SpecificationBuilder<Rental> {
    private final SpecificationProviderManager<Rental> rentalSpecificationProviderManager;

    @Override
    public Specification<Rental> build(RentalSearchParams searchParams) {
        Specification<Rental> spec = Specification.where(null);
        if (searchParams.user_id() != null && !searchParams.user_id().isEmpty()) {
            spec = spec.and(rentalSpecificationProviderManager
                    .getSpecificationProvider("user")
                    .getSpecification(searchParams.user_id()));
        }
        if (searchParams.is_active() != null) {
            spec = spec.and(rentalSpecificationProviderManager
                    .getSpecificationProvider("is_active")
                    .getSpecification(searchParams.is_active().toString()));
        }
        return spec;
    }
}

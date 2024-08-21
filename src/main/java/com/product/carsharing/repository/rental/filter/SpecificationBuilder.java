package com.product.carsharing.repository.rental.filter;

import com.product.carsharing.dto.rental.RentalSearchParams;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(RentalSearchParams searchParams);
}

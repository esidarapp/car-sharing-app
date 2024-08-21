package com.product.carsharing.service.rental;

import com.product.carsharing.dto.rental.CreateRentalDto;
import com.product.carsharing.dto.rental.RentalDto;
import com.product.carsharing.dto.rental.RentalSearchParams;
import com.product.carsharing.dto.rental.ReturnRentalDto;
import java.util.List;

public interface RentalService {
    RentalDto save(CreateRentalDto createRentalDto, Long id);

    List<RentalDto> getActiveRentals(Long userId);

    List<RentalDto> getAllRentals();

    List<RentalDto> search(RentalSearchParams params);

    RentalDto findById(Long rentalId);

    void returnRental(ReturnRentalDto returnRentalDto);

}

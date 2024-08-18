package mate.academy.carsharing.service.rental;

import java.util.List;
import mate.academy.carsharing.dto.rental.CreateRentalDto;
import mate.academy.carsharing.dto.rental.RentalDto;
import mate.academy.carsharing.dto.rental.RentalSearchParams;
import mate.academy.carsharing.dto.rental.ReturnRentalDto;

public interface RentalService {
    RentalDto save(CreateRentalDto createRentalDto, Long id);

    List<RentalDto> getActiveRentals(Long userId);

    List<RentalDto> search(RentalSearchParams params);

    RentalDto findById(Long rentalId);

    void returnRental(ReturnRentalDto returnRentalDto);

}

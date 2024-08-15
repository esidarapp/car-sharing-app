package mate.academy.carsharing.service.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.carsharing.dto.rental.CreateRentalDto;
import mate.academy.carsharing.dto.rental.RentalDto;
import mate.academy.carsharing.dto.rental.RentalSearchParams;
import mate.academy.carsharing.dto.rental.ReturnRentalDto;
import mate.academy.carsharing.exception.DataProcessingException;
import mate.academy.carsharing.exception.EntityNotFoundException;
import mate.academy.carsharing.mapper.RentalMapper;
import mate.academy.carsharing.model.Car;
import mate.academy.carsharing.model.Rental;
import mate.academy.carsharing.model.User;
import mate.academy.carsharing.repository.car.CarRepository;
import mate.academy.carsharing.repository.rental.RentalRepository;
import mate.academy.carsharing.repository.rental.filter.RentalSpecificationBuilder;
import mate.academy.carsharing.repository.user.UserRepository;
import mate.academy.carsharing.service.RentalService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final RentalMapper rentalMapper;
    private final RentalSpecificationBuilder rentalSpecificationBuilder;

    @Override
    public RentalDto save(CreateRentalDto createRentalDto, Long userId) {
        Car car = carRepository.findById(createRentalDto.carId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find car by id " + createRentalDto.carId()));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find user by id " + userId));

        Rental rental = rentalMapper.toModel(createRentalDto,car,user);
        validateCarInventory(car);
        validateRentalDates(rental);
        car.setInventory(car.getInventory() - 1);
        carRepository.save(car);
        rentalRepository.save(rental);
        return rentalMapper.toDto(rental);
    }

    @Override
    public List<RentalDto> getActiveRentals(Long userId) {
        RentalSearchParams params = new RentalSearchParams(userId.toString(),true);
        Specification<Rental> rentalSpecification = rentalSpecificationBuilder.build(params);
        List<Rental> rentals = rentalRepository.findAll(rentalSpecification);
        return rentalMapper.toDtoList(rentals);
    }

    @Override
    public List<RentalDto> search(RentalSearchParams params) {
        Specification<Rental> rentalSpecification = rentalSpecificationBuilder.build(params);
        List<Rental> rentals = rentalRepository.findAll(rentalSpecification);
        return rentalMapper.toDtoList(rentals);
    }

    @Override
    public RentalDto findById(Long rentalId) {
        Rental rental = findRentalById(rentalId);
        return rentalMapper.toDto(rental);
    }

    @Override
    @Transactional
    public void returnRental(ReturnRentalDto returnRentalDto) {
        Rental rental = findRentalById(returnRentalDto.rentalId());
        if (rental.getActualReturnDate() != null) {
            throw new DataProcessingException(
                    "Rental with id " + returnRentalDto.rentalId()
                            + " already returned");
        }
        rental.setActualReturnDate(returnRentalDto.actualReturnDate());
        rentalRepository.save(rental);

        Car car = rental.getCar();
        car.setInventory(car.getInventory() + 1);
        carRepository.save(car);
    }

    private void validateRentalDates(Rental rental) {
        LocalDate rentalDate = rental.getRentalDate();
        LocalDate returnDate = rental.getReturnDate();

        if (rentalDate.isAfter(returnDate) || rentalDate.isEqual(returnDate)) {
            throw new DataProcessingException(
                    "Return date must be after the rental date.");
        }
        long daysBetween = ChronoUnit.DAYS.between(rentalDate, returnDate);
        if (daysBetween < 1) {
            throw new DataProcessingException(
                    "Return date must be at least one day after the rental date.");
        }
    }

    private void validateCarInventory(Car car) {
        if (!(car.getInventory() >= 1)) {
            throw new DataProcessingException(
                    "Car inventory must be at least one");
        }
    }

    private Rental findRentalById(Long rentalId) {
        return rentalRepository.findById(rentalId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find rental by id " + rentalId));
    }
}

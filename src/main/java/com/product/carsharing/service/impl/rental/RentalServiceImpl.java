package com.product.carsharing.service.impl.rental;

import com.product.carsharing.dto.rental.CreateRentalDto;
import com.product.carsharing.dto.rental.RentalDto;
import com.product.carsharing.dto.rental.RentalSearchParams;
import com.product.carsharing.dto.rental.ReturnRentalDto;
import com.product.carsharing.exception.DataProcessingException;
import com.product.carsharing.exception.EntityNotFoundException;
import com.product.carsharing.mapper.RentalMapper;
import com.product.carsharing.model.Car;
import com.product.carsharing.model.Rental;
import com.product.carsharing.model.User;
import com.product.carsharing.repository.car.CarRepository;
import com.product.carsharing.repository.rental.RentalRepository;
import com.product.carsharing.repository.rental.filter.RentalSpecificationBuilder;
import com.product.carsharing.repository.user.UserRepository;
import com.product.carsharing.service.impl.notification.RentalNotificationSender;
import com.product.carsharing.service.rental.RentalService;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
    private final RentalNotificationSender rentalNotificationService;
    private final RentalValidator rentalValidator;
    private final InventoryService inventoryService;

    @Override
    public RentalDto save(CreateRentalDto createRentalDto, Long userId) {
        Car car = carRepository.findById(createRentalDto.carId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find car by id " + createRentalDto.carId()));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find user by id " + userId));

        Rental rental = rentalMapper.toModel(createRentalDto, car, user);
        rentalValidator.validateRentalDates(rental);
        inventoryService.decrementInventory(car);
        Rental savedRental = rentalRepository.save(rental);
        rentalNotificationService.sendNewRentalNotification(savedRental);
        return rentalMapper.toDto(savedRental);
    }

    @Override
    public List<RentalDto> getActiveRentals(Long userId) {
        RentalSearchParams params = new RentalSearchParams(userId.toString(), true);
        Specification<Rental> rentalSpecification = rentalSpecificationBuilder.build(params);
        List<Rental> rentals = rentalRepository.findAll(rentalSpecification);
        return rentalMapper.toDtoList(rentals);
    }

    @Override
    public List<RentalDto> getAllRentals() {
        List<Rental> rentals = rentalRepository.findAll();
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
            throw new DataProcessingException("Rental with id "
                    + returnRentalDto.rentalId() + " already returned");
        }
        rental.setActualReturnDate(returnRentalDto.actualReturnDate());
        rentalRepository.save(rental);

        Car car = rental.getCar();
        inventoryService.incrementInventory(car);
    }

    private Rental findRentalById(Long rentalId) {
        return rentalRepository.findById(rentalId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find rental by id " + rentalId));
    }

}

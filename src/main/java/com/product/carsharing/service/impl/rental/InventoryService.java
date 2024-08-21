package com.product.carsharing.service.impl.rental;

import com.product.carsharing.exception.DataProcessingException;
import com.product.carsharing.model.Car;
import com.product.carsharing.repository.car.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private static final String INVENTORY_VALIDATION = "Car inventory must be at least one.";

    private final CarRepository carRepository;

    public void decrementInventory(Car car) {
        if (car.getInventory() < 1) {
            throw new DataProcessingException(INVENTORY_VALIDATION);
        }
        car.setInventory(car.getInventory() - 1);
        carRepository.save(car);
    }

    public void incrementInventory(Car car) {
        car.setInventory(car.getInventory() + 1);
        carRepository.save(car);
    }
}

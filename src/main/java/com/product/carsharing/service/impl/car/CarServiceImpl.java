package com.product.carsharing.service.impl.car;

import com.product.carsharing.dto.car.CarDto;
import com.product.carsharing.dto.car.CreateCarRequestDto;
import com.product.carsharing.dto.car.UpdateCarRequestDto;
import com.product.carsharing.exception.EntityNotFoundException;
import com.product.carsharing.mapper.CarMapper;
import com.product.carsharing.model.Car;
import com.product.carsharing.repository.car.CarRepository;
import com.product.carsharing.service.car.CarService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarMapper carMapper;
    private final CarRepository carRepository;

    @Override
    public CarDto save(CreateCarRequestDto requestDto) {
        Car car = carMapper.toModel(requestDto);
        carRepository.save(car);
        return carMapper.toDto(car);
    }

    @Override
    public List<CarDto> findAll(Pageable pageable) {
        return carRepository.findAll(pageable).stream()
                .map(carMapper::toDto)
                .toList();
    }

    @Override
    public CarDto findById(Long id) {
        Car car = findCarById(id);
        return carMapper.toDto(car);
    }

    @Override
    public CarDto updateById(Long id, UpdateCarRequestDto requestDto) {
        Car car = findCarById(id);
        carMapper.updateCarFromDto(requestDto,car);
        carRepository.save(car);
        return carMapper.toDto(car);
    }

    @Override
    public void deleteById(Long id) {
        carRepository.deleteById(id);
    }

    private Car findCarById(Long id) {
        return carRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find car by id " + id)
        );
    }
}

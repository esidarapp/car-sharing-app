package com.product.carsharing.service.car;

import com.product.carsharing.dto.car.CarDto;
import com.product.carsharing.dto.car.CreateCarRequestDto;
import com.product.carsharing.dto.car.UpdateCarRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CarService {
    CarDto save(CreateCarRequestDto requestDto);

    List<CarDto> findAll(Pageable pageable);

    CarDto findById(Long id);

    CarDto updateById(Long id, UpdateCarRequestDto requestDto);

    void deleteById(Long id);
}

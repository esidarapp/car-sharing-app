package mate.academy.carsharing.service;

import java.util.List;
import mate.academy.carsharing.dto.car.CarDto;
import mate.academy.carsharing.dto.car.CreateCarRequestDto;
import mate.academy.carsharing.dto.car.UpdateCarRequestDto;
import org.springframework.data.domain.Pageable;

public interface CarService {
    CarDto save(CreateCarRequestDto requestDto);

    List<CarDto> findAll(Pageable pageable);

    CarDto findById(Long id);

    CarDto updateById(Long id, UpdateCarRequestDto requestDto);

    void deleteById(Long id);
}

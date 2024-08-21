package com.product.carsharing.mapper;

import com.product.carsharing.config.MapperConfig;
import com.product.carsharing.dto.car.CarDto;
import com.product.carsharing.dto.car.CreateCarRequestDto;
import com.product.carsharing.dto.car.UpdateCarRequestDto;
import com.product.carsharing.model.Car;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = "spring")
public interface CarMapper {
    Car toModel(CreateCarRequestDto requestDto);

    CarDto toDto(Car car);

    List<CarDto> toDtoList(List<Car> cars);

    void updateCarFromDto(UpdateCarRequestDto requestDto, @MappingTarget Car car);
}

package mate.academy.carsharing.mapper;

import java.util.List;
import mate.academy.carsharing.config.MapperConfig;
import mate.academy.carsharing.dto.car.CarDto;
import mate.academy.carsharing.dto.car.CreateCarRequestDto;
import mate.academy.carsharing.dto.car.UpdateCarRequestDto;
import mate.academy.carsharing.model.Car;
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

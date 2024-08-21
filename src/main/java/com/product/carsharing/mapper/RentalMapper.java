package com.product.carsharing.mapper;

import com.product.carsharing.config.MapperConfig;
import com.product.carsharing.dto.rental.CreateRentalDto;
import com.product.carsharing.dto.rental.RentalDto;
import com.product.carsharing.model.Car;
import com.product.carsharing.model.Rental;
import com.product.carsharing.model.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = "spring",
        uses = {CarMapper.class, UserMapper.class})
public interface RentalMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "car", target = "car")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "createRentalDto.returnDate", target = "returnDate")
    Rental toModel(CreateRentalDto createRentalDto, Car car, User user);

    @Mapping(target = "carId", source = "car.id")
    @Mapping(target = "userId", source = "user.id")
    RentalDto toDto(Rental rental);

    List<RentalDto> toDtoList(List<Rental> rentalList);
}

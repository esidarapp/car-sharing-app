package mate.academy.carsharing.mapper;

import java.util.List;
import mate.academy.carsharing.config.MapperConfig;
import mate.academy.carsharing.dto.rental.CreateRentalDto;
import mate.academy.carsharing.dto.rental.RentalDto;
import mate.academy.carsharing.model.Car;
import mate.academy.carsharing.model.Rental;
import mate.academy.carsharing.model.User;
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

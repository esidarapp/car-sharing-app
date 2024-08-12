package mate.academy.carsharing.dto.car;

import java.math.BigDecimal;
import mate.academy.carsharing.model.Car;

public record CarDto(
        Long id,
        String model,
        String brand,
        Car.CarType carType,
        Integer inventory,
        BigDecimal dailyFee
) {
}

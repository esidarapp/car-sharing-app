package mate.academy.carsharing.dto.car;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import mate.academy.carsharing.model.Car;

public record CreateCarRequestDto(
        @NotBlank
        String model,
        @NotBlank
        String brand,
        @NotNull
        Car.CarType carType,
        @NotNull
        Integer inventory,
        @NotNull
        BigDecimal dailyFee
) {
}

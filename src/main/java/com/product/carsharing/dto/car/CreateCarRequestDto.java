package com.product.carsharing.dto.car;

import com.product.carsharing.model.Car;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

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

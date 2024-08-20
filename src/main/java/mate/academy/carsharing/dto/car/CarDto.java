package mate.academy.carsharing.dto.car;

import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mate.academy.carsharing.model.Car;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CarDto {
    private Long id;
    private String model;
    private String brand;
    private Car.CarType carType;
    private Integer inventory;
    private BigDecimal dailyFee;
}

package mate.academy.carsharing.mapper;

import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import java.util.List;
import mate.academy.carsharing.config.MapperConfig;
import mate.academy.carsharing.dto.payment.PaymentDto;
import mate.academy.carsharing.model.Payment;
import mate.academy.carsharing.model.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = "spring")
public interface PaymentMapper {
    @Mapping(source = "rental.id", target = "rentalId")
    PaymentDto toDto(Payment payment);

    @Mapping(source = "session.id", target = "sessionId")
    @Mapping(source = "session.url", target = "sessionUrl")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    Payment toModel(Rental rental,
                    Session session,
                    BigDecimal amount,
                    Payment.Type type);

    List<PaymentDto> toDtoList(List<Payment> paymentList);
}

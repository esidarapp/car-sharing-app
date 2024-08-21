package com.product.carsharing.mapper;

import com.product.carsharing.config.MapperConfig;
import com.product.carsharing.dto.payment.PaymentDto;
import com.product.carsharing.model.Payment;
import com.product.carsharing.model.Rental;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import java.util.List;
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

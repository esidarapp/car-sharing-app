package mate.academy.carsharing.service.stripe;

import java.util.List;
import mate.academy.carsharing.dto.payment.PaymentDto;
import mate.academy.carsharing.dto.payment.PaymentRequestDto;

public interface PaymentService {
    PaymentDto createPaymentSession(PaymentRequestDto dto);

    List<PaymentDto> getPayments(Long userId);

    List<PaymentDto> getAllPayments();

    void handlePaymentSuccess(String sessionId);

    void handlePaymentCancel(String sessionId);
}

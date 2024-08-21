package com.product.carsharing.service.stripe;

import com.product.carsharing.dto.payment.PaymentDto;
import com.product.carsharing.dto.payment.PaymentRequestDto;
import java.util.List;

public interface PaymentService {
    PaymentDto createPaymentSession(PaymentRequestDto dto);

    List<PaymentDto> getPayments(Long userId);

    List<PaymentDto> getAllPayments();

    void handlePaymentSuccess(String sessionId);

    void handlePaymentCancel(String sessionId);
}

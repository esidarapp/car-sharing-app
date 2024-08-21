package com.product.carsharing.service.impl.stripe;

import com.product.carsharing.exception.DataProcessingException;
import com.product.carsharing.exception.EntityNotFoundException;
import com.product.carsharing.model.Payment;
import com.product.carsharing.model.Rental;
import com.product.carsharing.repository.payment.PaymentRepository;
import com.product.carsharing.service.stripe.AmountHandler;
import com.product.carsharing.service.stripe.PaymentProcessingService;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentProcessingServiceImpl implements PaymentProcessingService {
    private static final String PAYMENT_STATUS_PAID = "paid";
    private static final String PAYMENT_STATUS_CANCELED = "canceled";
    private static final String PAYMENT_STATUS_FAILED = "failed";

    private final PaymentRepository paymentRepository;
    private final HashMap<Payment.Type, AmountHandler> handlers;

    @Override
    public void handlePaymentSuccess(String sessionId) {
        try {
            Session session = Session.retrieve(sessionId);
            if (PAYMENT_STATUS_PAID.equals(session.getPaymentStatus())) {
                Payment payment = paymentRepository.findBySessionId(sessionId)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Payment not found for session: " + sessionId));
                payment.setStatus(Payment.Status.PAID);
                paymentRepository.save(payment);
            }
        } catch (Exception e) {
            throw new DataProcessingException("Failed to handle payment success", e);
        }
    }

    @Override
    public void handlePaymentCancel(String sessionId) {
        try {
            Session session = Session.retrieve(sessionId);
            if (PAYMENT_STATUS_CANCELED.equals(session.getPaymentStatus())
                    || PAYMENT_STATUS_FAILED.equals(session.getPaymentStatus())) {
                Payment payment = paymentRepository.findBySessionId(sessionId)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Payment not found for session: " + sessionId));
                payment.setStatus(Payment.Status.FAILED);
                paymentRepository.save(payment);
            }
        } catch (Exception e) {
            throw new DataProcessingException("Failed to handle payment cancel", e);
        }
    }

    @Override
    public BigDecimal getAmount(Rental rental, Payment.Type type) {
        AmountHandler amountHandler = handlers.get(type);
        return amountHandler.getAmount(rental);
    }
}

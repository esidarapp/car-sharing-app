package com.product.carsharing.service.impl.stripe;

import com.product.carsharing.dto.payment.PaymentDto;
import com.product.carsharing.dto.payment.PaymentRequestDto;
import com.product.carsharing.exception.DataProcessingException;
import com.product.carsharing.exception.EntityNotFoundException;
import com.product.carsharing.mapper.PaymentMapper;
import com.product.carsharing.model.Payment;
import com.product.carsharing.model.Rental;
import com.product.carsharing.repository.payment.PaymentRepository;
import com.product.carsharing.repository.rental.RentalRepository;
import com.product.carsharing.service.stripe.PaymentProcessingService;
import com.product.carsharing.service.stripe.PaymentService;
import com.product.carsharing.service.stripe.StripeService;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final RentalRepository rentalRepository;
    private final PaymentMapper paymentMapper;
    private final StripeService stripeService;
    private final PaymentProcessingService paymentProcessingService;

    @Override
    public PaymentDto createPaymentSession(PaymentRequestDto dto) {
        Rental rental = rentalRepository.findById(dto.rentalId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find rental by id " + dto.rentalId()));
        BigDecimal amount = paymentProcessingService.getAmount(rental, dto.type());
        try {
            Session session = stripeService.createSession(amount);
            Payment payment = paymentMapper.toModel(rental, session, amount, dto.type());
            paymentRepository.save(payment);
            return paymentMapper.toDto(payment);
        } catch (Exception e) {
            throw new DataProcessingException("Failed to create payment session", e);
        }
    }

    @Override
    public List<PaymentDto> getPayments(Long userId) {
        List<Payment> paymentList = paymentRepository.findByRental_User_Id(userId);
        return paymentMapper.toDtoList(paymentList);
    }

    @Override
    public List<PaymentDto> getAllPayments() {
        List<Payment> paymentList = paymentRepository.findAll();
        return paymentMapper.toDtoList(paymentList);
    }

    @Override
    public void handlePaymentSuccess(String sessionId) {
        paymentProcessingService.handlePaymentSuccess(sessionId);
    }

    @Override
    public void handlePaymentCancel(String sessionId) {
        paymentProcessingService.handlePaymentCancel(sessionId);
    }
}

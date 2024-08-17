package mate.academy.carsharing.service.impl.stripe;

import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.carsharing.dto.payment.PaymentDto;
import mate.academy.carsharing.dto.payment.PaymentRequestDto;
import mate.academy.carsharing.exception.DataProcessingException;
import mate.academy.carsharing.exception.EntityNotFoundException;
import mate.academy.carsharing.mapper.PaymentMapper;
import mate.academy.carsharing.model.Payment;
import mate.academy.carsharing.model.Rental;
import mate.academy.carsharing.repository.payment.PaymentRepository;
import mate.academy.carsharing.repository.rental.RentalRepository;
import mate.academy.carsharing.service.stripe.PaymentProcessingService;
import mate.academy.carsharing.service.stripe.PaymentService;
import mate.academy.carsharing.service.stripe.StripeService;
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

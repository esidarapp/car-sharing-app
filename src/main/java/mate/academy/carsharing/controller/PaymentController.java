package mate.academy.carsharing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.carsharing.dto.payment.PaymentDto;
import mate.academy.carsharing.dto.payment.PaymentRequestDto;
import mate.academy.carsharing.model.User;
import mate.academy.carsharing.service.stripe.PaymentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Payment management", description = "Endpoints for managing payments")
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('MANAGER')")
    @Operation(summary = "Get payments",
            description = "Retrieve a list of payments."
                    + " Managers get all payments, while customers get their own.")
    public List<PaymentDto> getPayments(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        if (user.getRole() == User.Role.MANAGER) {
            return paymentService.getAllPayments();
        } else {
            return paymentService.getPayments(user.getId());
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('MANAGER')")
    @Operation(summary = "Create payment session",
            description = "Create a new payment session based on the provided request data.")
    public PaymentDto createPaymentSession(@RequestBody @Valid PaymentRequestDto dto) {
        return paymentService.createPaymentSession(dto);
    }

    @GetMapping("/success")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('MANAGER')")
    @Operation(summary = "Handle payment success",
            description = "Handle the success of a payment based on the provided session ID.")
    public void handlePaymentSuccess(@RequestParam String sessionId) {
        paymentService.handlePaymentSuccess(sessionId);
    }

    @GetMapping("/cancel")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('MANAGER')")
    @Operation(summary = "Handle payment cancellation",
            description = "Handle the cancellation of a payment based on the provided session ID.")
    public void handlePaymentCancel(@RequestParam String sessionId) {
        paymentService.handlePaymentCancel(sessionId);
    }
}

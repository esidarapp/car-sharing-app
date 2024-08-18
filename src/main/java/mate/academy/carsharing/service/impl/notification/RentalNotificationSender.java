package mate.academy.carsharing.service.impl.notification;

import lombok.RequiredArgsConstructor;
import mate.academy.carsharing.model.Rental;
import mate.academy.carsharing.service.notification.NotificationService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RentalNotificationSender {
    private static final String SPACE = " ";
    private final NotificationService notificationService;

    public void sendNewRentalNotification(Rental rental) {
        String message = String.format(
                NotificationConstants.NEW_RENTAL_MESSAGE_FORMAT,
                rental.getId(),
                rental.getCar().getBrand() + SPACE + rental.getCar().getModel(),
                rental.getUser().getEmail(),
                rental.getRentalDate(),
                rental.getReturnDate()
        );
        notificationService.sendNotification(message);
    }

}

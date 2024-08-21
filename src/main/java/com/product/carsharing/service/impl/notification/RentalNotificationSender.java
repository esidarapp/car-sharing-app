package com.product.carsharing.service.impl.notification;

import com.product.carsharing.model.Rental;
import com.product.carsharing.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
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

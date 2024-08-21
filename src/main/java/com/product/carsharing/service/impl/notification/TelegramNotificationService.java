package com.product.carsharing.service.impl.notification;

import com.product.carsharing.model.Rental;
import com.product.carsharing.repository.rental.RentalRepository;
import com.product.carsharing.service.notification.MyLongPollingBot;
import com.product.carsharing.service.notification.NotificationService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramNotificationService implements NotificationService {
    private final MyLongPollingBot myLongPollingBot;
    private final RentalRepository rentalRepository;

    @Value("${telegram.chat.id}")
    private String chatId;

    @Override
    public void sendNotification(String message) {
        myLongPollingBot.sendResponse(chatId, message);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void checkOverdueRentals() {
        List<Rental> overdueRentals = findOverdueRentals();

        if (!overdueRentals.isEmpty()) {
            sendNotification(createOverdueRentalsMessage(overdueRentals));
        } else {
            sendNotification(NotificationConstants.NO_OVERDUE_RENTAL_MESSAGE);
        }
    }

    private List<Rental> findOverdueRentals() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        return rentalRepository.findAll((root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.lessThanOrEqualTo(root.get("returnDate"), tomorrow),
                        criteriaBuilder.isNull(root.get("actualReturnDate"))
                )
        );
    }

    private String createOverdueRentalsMessage(List<Rental> overdueRentals) {
        StringBuilder message = new StringBuilder(NotificationConstants.OVERDUE_RENTAL_MESSAGE);
        for (Rental rental : overdueRentals) {
            message.append(String.format(NotificationConstants.RENTAL_DETAILS_FORMAT,
                    rental.getId(),
                    rental.getRentalDate(),
                    rental.getReturnDate(),
                    rental.getUser().getEmail()));
        }
        return message.toString();
    }
}

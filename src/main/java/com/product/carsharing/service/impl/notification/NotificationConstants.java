package com.product.carsharing.service.impl.notification;

public class NotificationConstants {
    public static final String OVERDUE_RENTAL_MESSAGE = "You have overdue rentals:\n";
    public static final String NO_OVERDUE_RENTAL_MESSAGE = "There are no overdue rentals today.";
    public static final String RENTAL_DETAILS_FORMAT = "Rental #%d\nRental date: %s\nReturn date:"
            + " %s\nClient: %s\n\n";
    public static final String NEW_RENTAL_MESSAGE_FORMAT =
            "New rental created:\n"
                    + "Rental ID: %d\n"
                    + "Car: %s\n"
                    + "User: %s\n"
                    + "Rental Date: %s\n"
                    + "Return Date: %s";
}

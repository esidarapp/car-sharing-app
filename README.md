# Car Sharing API 🚗

Welcome to the Car Sharing API, a comprehensive backend service designed to manage car rentals, users, payments, and notifications within a car-sharing platform. This project leverages modern technologies and follows industry best practices to deliver a robust, secure, and scalable API.

## Introduction

The Car Sharing API was inspired by the growing need for flexible and efficient solutions for car-sharing platforms. This API addresses common challenges such as secure user authentication, role-based access control, efficient car inventory management, and seamless payment processing. Whether you're building a new car-sharing service or enhancing an existing one, this API serves as a reliable foundation.

## Technologies Used

- **Spring Boot** 🌱: For building the RESTful web services.
- **Spring Security** 🔒: To handle authentication and authorization, including JWT support.
- **Spring Data JPA** 💾: For database operations.
- **MySQL** 🗄️: As the main database.
- **Swagger** 📜: For API documentation.
- **Docker** 🐳: To containerize the application.
- **MapStruct** 🔄: For object mapping.
- **JUnit & MockMvc** 🧪: For unit and integration testing.
- **Stripe API** 💳: For handling payment processing.
- **Telegram API** 📲: For sending notifications.

## Features

### Car Management 🚗

- **CRUD Operations**: Create, read, update, and delete cars in the inventory.
- **Role-Based Access Control**: Only admin users can create, update, or delete cars. All users, including unauthenticated ones, can list available cars.
- **Car Availability Validation**: Ensure cars are available (inventory > 0) before creating a rental.

### User Management 👥

- **Registration & Authentication**: Users can register and log in using JWT-based authentication.
- **Role-Based Access Control**: Differentiated access levels for customers and admins.
- **Profile Management**: Users can update their profile information.

### Rental Management 🛒

- **Rental Operations**: Create, list, update, and return rentals.
- **Rental Validation**: Validate car availability and user roles before processing rentals.
- **Return Functionality**: Ensure rentals cannot be returned twice and update car inventory upon return.
- **Rental Filtering**: Filter rentals by various criteria, such as user ID and active status.
- **Overdue Rental Notifications**: Scheduled tasks to notify users of overdue rentals.

### Payment Processing 💳

- **Stripe Integration**: Seamless integration with Stripe to handle payments.
- **CRUD Operations**: Create and manage payment sessions.
- **Role-Based Access Control**: Customers can only view their payments, while managers can view all payments.
- **Fine Calculation**: Calculate fines for overdue rentals based on configurable multipliers.

### Notifications 📲

- **Telegram Integration**: Notifications are sent to a Telegram chat for key events, such as rental creation and overdue rentals.
- **Scheduled Notifications**: Daily checks for overdue rentals with notifications sent to the Telegram chat.

### API Documentation 📖

- **Swagger**: Interactive API documentation available at `/swagger-ui.html`.

## Getting Started 🚀

### Prerequisites

- **Java 21**
- **Maven**
- **Docker**
- **Stripe Account** (Test Mode)
- **Telegram Bot** (For Notifications)

### Setup

1. **Clone the repository**:
   ```sh
   git clone https://github.com/esidarapp/car-sharing-app
2. **Navigate to the project directory**:
   ```sh
   cd car-sharing-app
3. **Build the project**:
   ```sh
   mvn clean install
4. **Start the application using Docker Compose**:
   ```sh
   docker-compose up

### Connecting to a Custom Database 🗄️
To connect to a custom MySQL database, update the application.properties file with your database details:
```properties
spring.datasource.url=jdbc:mysql://<YOUR_DB_HOST>:<YOUR_DB_PORT>/<YOUR_DB_NAME>
spring.datasource.username=<YOUR_DB_USERNAME>
spring.datasource.password=<YOUR_DB_PASSWORD>
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```

## Stripe Configuration 💳

To configure Stripe for payment processing, add the following settings to your `application.properties` file:

```properties
stripe.api.key=<STRIPE_API_KEY>
stripe.success.url=<STRIPE_SUCCESS_URL>
stripe.cancel.url=<STRIPE_CANCEL_URL>
```

## Telegram Bot Configuration 📲

To enable Telegram notifications, configure the following properties in your `application.properties` file:

```properties
telegram.bot.username=<TELEGRAM_BOT_USERNAME>
telegram.bot.token=<TELEGRAM_BOT_TOKEN>
telegram.chat.id=<TELEGRAM_CHAT_ID>
```

### Entity Relationships
Here's a diagram representing the relationships between the entities in the Car Sharing API:
![Entity Relationships Diagram](architecture.png)



## Conclusion

The Car Sharing API provides a strong foundation for building a scalable, secure, and feature-rich car-sharing platform. By leveraging modern technologies and adhering to best practices, this API is equipped to handle the demands of a growing car-sharing service. Whether you're looking to deploy this API as-is or customize it to meet specific business needs, it offers the flexibility and reliability required for success.

Feel free to contribute to the project, report issues, or submit feature requests via GitHub. Happy coding! 🚀

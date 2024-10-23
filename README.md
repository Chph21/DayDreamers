# Daydreamer - Photoshoot Booking & Studio Management System

Daydreamer is a Java-based backend application built with Spring Boot, designed to manage, schedule, and handle users' photoshoot booking requests and studios' information efficiently. It provides a streamlined system for managing studios, clients, and bookings.

## Features

- **User Management**: Handle user registration, authentication, and profile management.
- **Studio Management**: Manage detailed information about photography studios, such as available equipment, studio locations, and pricing.
- **Booking System**: Enable users to schedule and book photoshoots with available studios.
- **Schedule Management**: Manage photoshoot schedules, ensuring no booking conflicts and seamless coordination between users and studios.

## Tech Stack

- **Java**: Backend logic development
- **Spring Boot**: Framework for building the backend API
- **Spring Data JPA**: ORM to interact with the database
- **Hibernate**: ORM tool for data persistence
- **PostgreSQL**: Database for storing user, booking, and studio information
- **Spring Security**: For authentication and authorization
- **REST API**: Provides endpoints for user, studio, and booking management

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL

### Setup

1. **Clone the repository**:

   ```bash
   git clone https://github.com/Chph21/DayDreamers.git
   ```

2. **Navigate to the project directory**:

   ```bash
   cd daydreamer
   ```

3. **Set up the database**:

   Create a new database for the project in PostgreSQL. Update the `application.yml` file with your database configuration.

4. **Build the project**:

   ```bash
   mvn clean install
   ```

5. **Run the application**:

   ```bash
   mvn spring-boot:run
   ```

6. **Access the application**:

   Once the application starts, you can access it at `http://localhost:8080`.

## API Endpoints

- **User Endpoints**:
  - POST `/account/register`: Register a new user
  - POST `/account/login`: Authenticate a user
  - GET `/accounts/{id}`: Get user details by ID

- **Studio Endpoints**:
  - POST `/studios`: Add a new studio
  - GET `/studios/{id}`: View studio details
  - PUT `/studios/{id}`: Update studio information
  - DELETE `/studios/{id}`: Remove a studio

- **Booking Endpoints**:
  - POST `/bookings`: Book a studio for a photoshoot
  - GET `/bookings/{id}`: Get booking details
  - PUT `/bookings/{id}`: Update booking information
  - DELETE `/bookings/{id}`: Cancel a booking

## Contribution Guidelines

- Fork the repository and create your branch from `main`.
- If you've added code that should be tested, add tests.
- Ensure the project passes all tests before submitting a pull request.

## License

This project is licensed under the MIT License. See the `LICENSE` file for more details.

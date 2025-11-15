# Samvad Backend - Spring Boot

## Overview
This is the Spring Boot backend for the Samvad messaging application, replacing Firebase services.

## Features
- User authentication with JWT
- Phone OTP verification
- User profile management
- Real-time messaging with WebSocket
- Image upload/download for profile pictures
- RESTful API endpoints

## Tech Stack
- Spring Boot 3.x
- Spring Security with JWT
- Spring WebSocket
- Spring Data JPA
- PostgreSQL/MySQL
- Twilio (for SMS OTP)

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven
- PostgreSQL/MySQL database

### Configuration
1. Update `application.properties` with your database credentials
2. Configure Twilio credentials for SMS OTP
3. Set JWT secret key

### Running the Application
```bash
mvn spring-boot:run
```

The server will start on `http://localhost:8080`

## API Endpoints

### Authentication
- `POST /api/auth/send-otp` - Send OTP to phone number
- `POST /api/auth/verify-otp` - Verify OTP and authenticate
- `POST /api/auth/refresh` - Refresh JWT token

### User Profile
- `GET /api/users/profile` - Get user profile
- `PUT /api/users/profile` - Update user profile
- `POST /api/users/profile/image` - Upload profile image
- `GET /api/users/profile/image/{userId}` - Get profile image

### Messaging
- WebSocket endpoint: `/ws` - Real-time messaging
- `GET /api/messages/{userId}` - Get message history



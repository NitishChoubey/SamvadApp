# 💬 Samvad - Real-Time Messaging Application

<div align="center">
  
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![WebSocket](https://img.shields.io/badge/WebSocket-010101?style=for-the-badge&logo=socketdotio&logoColor=white)

**A modern, full-stack real-time messaging application built with Jetpack Compose and Spring Boot**

[Features](#-features) • [Tech Stack](#-tech-stack) • [Quick Setup](#-quick-setup) • [Documentation](#-documentation) • [Screenshots](#-screenshots)

</div>

---

## 📋 Table of Contents

- [About](#-about)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Quick Setup](#-quick-setup)
- [Detailed Setup](#-detailed-setup)
- [Usage](#-usage)
- [API Documentation](#-api-documentation)
- [Troubleshooting](#-troubleshooting)
- [Contributing](#-contributing)
- [License](#-license)

---

## 📖 About

**Samvad** (संवाद - meaning "conversation" in Hindi) is a modern, full-stack real-time messaging application that enables users to communicate seamlessly through instant messaging. Built with cutting-edge technologies, it features a beautiful Material Design 3 UI on Android and a robust Spring Boot backend with WebSocket support for real-time communication.

### Key Highlights

- 🚀 **Real-time messaging** using WebSockets
- 🔐 **Secure authentication** with OTP and JWT tokens
- 📱 **Modern Android UI** built with Jetpack Compose
- 🏗️ **Clean Architecture** following MVVM pattern
- 🔄 **RESTful APIs** for all backend operations
- 💾 **PostgreSQL database** for data persistence
- 🎨 **Material Design 3** UI components
- 🔧 **Dependency Injection** with Hilt/Dagger

---

## ✨ Features

### 🔐 Authentication
- Phone number-based registration
- OTP verification (console-based for development)
- JWT token-based authentication
- Automatic token refresh
- Secure session management

### 💬 Messaging
- Real-time one-to-one messaging
- WebSocket-based instant delivery
- Message history persistence
- Read receipts support
- Typing indicators (ready to implement)
- Online/offline status tracking

### 👤 User Management
- User profile creation and updates
- Contact search functionality
- User presence detection
- Profile picture support (ready to implement)

### 🎨 UI/UX
- Modern Material Design 3 interface
- Dark/Light theme support
- Smooth animations and transitions
- Intuitive navigation
- Responsive layouts
- Bottom navigation bar

---

## 🛠 Tech Stack

### Android Frontend

| Technology | Description |
|------------|-------------|
| **Kotlin** | Primary programming language |
| **Jetpack Compose** | Modern declarative UI framework |
| **Material Design 3** | UI components and design system |
| **MVVM Architecture** | Clean architecture pattern |
| **Hilt/Dagger** | Dependency injection |
| **Retrofit** | HTTP client for REST APIs |
| **OkHttp** | WebSocket and HTTP client |
| **Kotlin Coroutines** | Asynchronous programming |
| **Flow** | Reactive data streams |
| **Room** | Local database (optional) |
| **Coil** | Image loading library |
| **Navigation Compose** | Navigation component |

### Backend (Spring Boot)

| Technology | Description |
|------------|-------------|
| **Java 17** | Programming language |
| **Spring Boot 3.2.0** | Backend framework |
| **Spring Security** | Authentication & authorization |
| **Spring Data JPA** | Data persistence |
| **Spring WebSocket** | Real-time communication |
| **PostgreSQL** | Relational database |
| **JWT (jjwt)** | Token-based authentication |
| **Lombok** | Boilerplate code reduction |
| **Maven** | Dependency management |

---

## 🏗 Architecture

### Android App Architecture

```
app/
├── data/
│   ├── local/        # Room database, SharedPreferences
│   ├── remote/       # API services, WebSocket
│   └── repository/   # Repository implementations
├── domain/
│   ├── model/        # Domain models
│   ├── repository/   # Repository interfaces
│   └── usecase/      # Business logic
├── presentation/
│   ├── auth/         # Login, OTP screens
│   ├── chat/         # Chat list, conversation
│   ├── profile/      # User profile screens
│   └── common/       # Shared UI components
└── di/               # Dependency injection modules
```

### Backend Architecture

```
backend/
├── config/           # Configuration classes
├── controller/       # REST API endpoints
├── dto/             # Data transfer objects
├── entity/          # JPA entities
├── repository/      # Database repositories
├── service/         # Business logic
├── security/        # JWT, authentication
└── websocket/       # WebSocket handlers
```

### Communication Flow

```
[Android App] <--REST API--> [Spring Boot Backend] <---> [PostgreSQL DB]
     |                              |
     └--------WebSocket-------------┘
```

---

## 🚀 Quick Setup

### Prerequisites

- **Java 17** or higher
- **Android Studio** (latest version)
- **PostgreSQL 14+** installed and running
- **Git** for version control
- **Android SDK 24+** (for app)

### 5-Minute Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/samvad.git
   cd samvad
   ```

2. **Setup PostgreSQL Database**
   ```bash
   # Create database and user
   psql -U postgres
   CREATE DATABASE samvad_db;
   CREATE USER samvad_user WITH PASSWORD 'samvad_password';
   GRANT ALL PRIVILEGES ON DATABASE samvad_db TO samvad_user;
   \q
   ```

3. **Configure Backend**
   ```bash
   cd backend/src/main/resources
   # Copy and edit application.properties
   # Update database credentials if needed
   ```

4. **Start Backend Server**
   ```bash
   cd backend
   mvn clean install
   java -jar target/samvad-backend-1.0.0.jar
   ```
   Or use the batch file:
   ```cmd
   start-backend.bat
   ```

5. **Open Android Project**
   ```bash
   # Open project in Android Studio
   # Wait for Gradle sync to complete
   # Run the app on emulator or device
   ```

6. **Start Testing**
   - Register with a phone number (e.g., `+919876543210`)
   - Check backend console for OTP
   - Enter OTP and start chatting!

📖 **For detailed setup instructions, see:** [FINAL_INSTALLATION_GUIDE.md](FINAL_INSTALLATION_GUIDE.md)

---

## 📚 Detailed Setup

### Backend Setup

#### 1. Database Configuration

Create `backend/src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/samvad_db
spring.datasource.username=samvad_user
spring.datasource.password=samvad_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT Configuration
jwt.secret=your-secret-key-here-make-it-long-and-secure
jwt.expiration=86400000

# Twilio Configuration (Optional)
twilio.account.sid=your_account_sid
twilio.auth.token=your_auth_token
twilio.phone.number=your_twilio_number
```

#### 2. Build and Run

```bash
cd backend
mvn clean install
java -jar target/samvad-backend-1.0.0.jar
```

The server will start on `http://localhost:8080`

### Android App Setup

#### 1. Configure Backend URL

Edit `app/build.gradle.kts`:

```kotlin
buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:8080/\"")
```

- For **Emulator**: Use `http://10.0.2.2:8080/`
- For **Real Device**: Use `http://YOUR_PC_IP:8080/`

#### 2. Sync and Build

1. Open project in Android Studio
2. Wait for Gradle sync
3. Build → Make Project
4. Run on emulator or device

---

## 📱 Usage

### Starting a Chat

1. **Register/Login**
   - Enter phone number
   - Get OTP from backend console logs
   - Verify OTP to login

2. **Find Contacts**
   - Use search functionality
   - Enter phone number of other user
   - Start conversation

3. **Send Messages**
   - Type message in input field
   - Press send button
   - Messages delivered in real-time via WebSocket

### Testing with Multiple Users

**Option 1: Multiple Emulators**
- Start 2 Android emulators
- Install app on both
- Register with different phone numbers
- Chat between them

**Option 2: Emulator + Real Device**
- Change BASE_URL to your PC's IP
- Install on both devices
- Register with different phone numbers
- Test real-time messaging

📖 **For detailed usage guide, see:** [HOW_TO_START_CHATTING.md](HOW_TO_START_CHATTING.md)

---

## 🔌 API Documentation

### Authentication Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/send-otp` | Send OTP to phone number |
| POST | `/api/auth/verify-otp` | Verify OTP and get JWT token |
| POST | `/api/auth/refresh` | Refresh JWT token |

### User Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | Get all users |
| GET | `/api/users/{userId}` | Get user profile |
| PUT | `/api/users/{userId}` | Update user profile |
| GET | `/api/users/phone/{phoneNumber}` | Find user by phone |

### Message Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/messages` | Send a message |
| GET | `/api/messages/chat/{userId}` | Get chat history |
| GET | `/api/messages/user/{userId}` | Get all messages for user |
| PUT | `/api/messages/{messageId}/read` | Mark message as read |

### WebSocket

| Endpoint | Description |
|----------|-------------|
| `/ws` | WebSocket connection for real-time messaging |

**Example: Connect to WebSocket**
```kotlin
val client = OkHttpClient()
val request = Request.Builder()
    .url("ws://10.0.2.2:8080/ws")
    .addHeader("Authorization", "Bearer $jwtToken")
    .build()
val webSocket = client.newWebSocket(request, webSocketListener)
```

---

## 🐛 Troubleshooting

### Common Issues

#### 1. KSP Failed with PROCESSING_ERROR

**Problem:** Kotlin Symbol Processing fails during build

**Solutions:**
- Clean and rebuild project
- Invalidate caches and restart Android Studio
- Check Hilt/Dagger annotations are correct
- Ensure all dependencies are properly configured

```bash
# Clean project
./gradlew clean

# Rebuild
./gradlew build
```

📖 See: [KSP_ERROR_FIX_SUMMARY.md](KSP_ERROR_FIX_SUMMARY.md)

#### 2. Backend Connection Failed

**Problem:** App cannot connect to backend

**Solutions:**
- Verify backend is running: `netstat -ano | findstr :8080`
- Check BASE_URL in `app/build.gradle.kts`
- For emulator, use `http://10.0.2.2:8080/`
- For real device, use PC's IP address
- Disable firewall or allow port 8080

#### 3. WebSocket Connection Issues

**Problem:** Real-time messages not working

**Solutions:**
- Check WebSocket endpoint: `ws://10.0.2.2:8080/ws`
- Verify JWT token is being sent
- Check backend logs for WebSocket connections
- Ensure Spring WebSocket is configured correctly

#### 4. Database Connection Error

**Problem:** Backend cannot connect to PostgreSQL

**Solutions:**
- Verify PostgreSQL is running
- Check database credentials in `application.properties`
- Ensure database `samvad_db` exists
- Test connection: `psql -U samvad_user -d samvad_db`

📖 See: [POSTGRESQL_SETUP.md](POSTGRESQL_SETUP.md)

#### 5. OTP Not Received

**Problem:** Cannot get OTP for login

**Solution:**
- OTP is printed in **backend console logs** (Twilio not configured)
- Look for: `OTP for +919876543210: 123456`
- Copy the 6-digit code and enter in app

---

## 📚 Documentation

This project includes comprehensive documentation:

| Document | Description |
|----------|-------------|
| [FINAL_INSTALLATION_GUIDE.md](FINAL_INSTALLATION_GUIDE.md) | Complete installation guide |
| [HOW_TO_START_CHATTING.md](HOW_TO_START_CHATTING.md) | Guide to start using the app |
| [POSTGRESQL_SETUP.md](POSTGRESQL_SETUP.md) | PostgreSQL database setup |
| [POSTGRESQL_QUICK_GUIDE.md](POSTGRESQL_QUICK_GUIDE.md) | Quick PostgreSQL reference |
| [KSP_ERROR_FIX_SUMMARY.md](KSP_ERROR_FIX_SUMMARY.md) | KSP build error solutions |
| [CHAT_FIX_SUMMARY.md](CHAT_FIX_SUMMARY.md) | Chat functionality fixes |
| [TESTING_GUIDE.md](TESTING_GUIDE.md) | Testing procedures |
| [START_TESTING_NOW.md](START_TESTING_NOW.md) | Quick testing guide |

---

## 🔧 Development

### Project Structure

```
Samvad/
├── app/                          # Android application
│   ├── src/main/
│   │   ├── java/com/example/samvad/
│   │   │   ├── data/            # Data layer
│   │   │   ├── domain/          # Domain layer
│   │   │   ├── presentation/    # UI layer
│   │   │   └── di/              # Dependency injection
│   │   └── res/                 # Resources
│   └── build.gradle.kts
├── backend/                      # Spring Boot backend
│   ├── src/main/
│   │   ├── java/com/example/samvad/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── entity/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── security/
│   │   └── resources/
│   └── pom.xml
├── gradle/                       # Gradle wrapper
├── docs/                         # Documentation
└── README.md
```

### Building from Source

#### Android App

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Install on connected device
./gradlew installDebug
```

#### Backend

```bash
cd backend

# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package JAR
mvn package

# Skip tests
mvn package -DskipTests
```

### Running Tests

#### Android Tests

```bash
# Unit tests
./gradlew test

# Instrumentation tests
./gradlew connectedAndroidTest
```

#### Backend Tests

```bash
cd backend
mvn test
```

---

## 🔐 Security Considerations

- JWT tokens are used for authentication
- Passwords/OTPs are never logged in production
- WebSocket connections are authenticated
- Database credentials should be externalized
- Use HTTPS in production
- Implement proper input validation
- Enable SQL injection protection (JPA)
- Use environment variables for secrets

---

## 🚀 Future Enhancements

- [ ] Group messaging support
- [ ] Voice/video calling
- [ ] File sharing (images, documents)
- [ ] Push notifications (FCM)
- [ ] End-to-end encryption
- [ ] Message reactions and replies
- [ ] User stories/status
- [ ] Advanced search and filters
- [ ] Message backup and restore
- [ ] Multi-device support
- [ ] Desktop application (Electron)
- [ ] Web application (React/Vue)

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Coding Standards

- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Write unit tests for new features
- Update documentation as needed
- Follow Material Design guidelines for UI

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👥 Authors

- **Your Name** - *Initial work* - [GitHub Profile](https://github.com/yourusername)

---

## 🙏 Acknowledgments

- **Android Jetpack** team for amazing tools
- **Spring Boot** community for excellent documentation
- **Material Design** team for design guidelines
- All open-source contributors

---

## 📞 Support

If you encounter any issues or have questions:

1. Check the [documentation](#-documentation)
2. Review [troubleshooting guide](#-troubleshooting)
3. Search existing [GitHub Issues](https://github.com/yourusername/samvad/issues)
4. Create a new issue with detailed information

---

## 📊 Project Status

🚧 **Active Development** - This project is under active development. Features are being added regularly.

---

<div align="center">

**Made with ❤️ by the Samvad Team**

⭐ Star this repo if you find it helpful!

[Report Bug](https://github.com/yourusername/samvad/issues) • [Request Feature](https://github.com/yourusername/samvad/issues)

</div>


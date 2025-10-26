# Weather App

A Spring Boot application that displays real-time weather information for multiple cities around the world with secure OAuth2 authentication via Auth0.

## 📋 Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [Application Flow](#application-flow)
- [API Integration](#api-integration)
- [Project Structure](#project-structure)
- [Troubleshooting](#troubleshooting)

## ✨ Features

- **Real-time Weather Data**: Fetches current weather information from OpenWeatherMap API
- **Multi-city Support**: Displays weather for 8 cities globally (Colombo, Tokyo, Liverpool, Paris, Sydney, Boston, Shanghai, Oslo)
- **Secure Authentication**: OAuth2 login integration with Auth0
- **Caching**: Efficient data caching using Caffeine cache to reduce API calls
- **Responsive UI**: Clean and user-friendly interface built with Thymeleaf templates
- **User Profile**: Displays authenticated user's name and email

## 🛠 Technology Stack

- **Java**: 21
- **Spring Boot**: 3.4.11
- **Spring Security**: OAuth2 Client
- **Thymeleaf**: Server-side template engine
- **Caffeine Cache**: In-memory caching
- **Auth0**: Authentication provider
- **OpenWeatherMap API**: Weather data provider
- **Maven**: Build and dependency management

## 📦 Prerequisites

Before running this application, ensure you have:

- **Java JDK 21** or higher installed
- **Maven 3.6+** installed
- **Auth0 Account** (free tier available at [auth0.com](https://auth0.com))
- **OpenWeatherMap API Key** (free at [openweathermap.org](https://openweathermap.org/api))
- Internet connection for API calls

## 🚀 Installation

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd Weather-App
   ```

2. **Verify Java version**:
   ```bash
   java -version
   ```
   Ensure it shows Java 21 or higher.

3. **Build the project**:
   ```bash
   mvnw clean install
   ```
   Or on Windows:
   ```bash
   mvnw.cmd clean install
   ```

## ⚙️ Configuration

### 1. Auth0 Setup

1. Create an account at [Auth0](https://auth0.com)
2. Create a new application (Regular Web Application)
3. Configure the following settings in your Auth0 dashboard:
   - **Allowed Callback URLs**: `http://localhost:8080/login/oauth2/code/auth0`
   - **Allowed Logout URLs**: `http://localhost:8080/`
   - **Allowed Web Origins**: `http://localhost:8080`

4. Note down your:
   - Domain (e.g., `dev-xxxxx.us.auth0.com`)
   - Client ID
   - Client Secret

See [AUTH0_SETUP.md](AUTH0_SETUP.md) for detailed instructions.

### 2. Application Properties

Update `src/main/resources/application.properties` with your credentials:

```properties
# Weather API Configuration
weather.api.key=YOUR_OPENWEATHERMAP_API_KEY

# Auth0 Configuration
auth0.domain=YOUR_AUTH0_DOMAIN
auth0.clientId=YOUR_AUTH0_CLIENT_ID
auth0.clientSecret=YOUR_AUTH0_CLIENT_SECRET

# OAuth2 Configuration (uses the above Auth0 properties)
spring.security.oauth2.client.registration.auth0.client-id=${auth0.clientId}
spring.security.oauth2.client.registration.auth0.client-secret=${auth0.clientSecret}
spring.security.oauth2.client.registration.auth0.scope=openid,profile,email
spring.security.oauth2.client.registration.auth0.redirect-uri=http://localhost:8080/login/oauth2/code/auth0
spring.security.oauth2.client.registration.auth0.client-name=Auth0
spring.security.oauth2.client.provider.auth0.issuer-uri=https://${auth0.domain}/

# Thymeleaf Configuration
spring.thymeleaf.cache=false

# Debug Settings (optional)
debug=false
logging.level.org.springframework.security=INFO
```

### 3. Cities Configuration

The application reads city codes from `src/main/resources/cities.json`. You can modify this file to add or remove cities:

```json
{
  "List": [
    {"CityCode": "1248991", "CityName": "Colombo", "Temp": "33.0", "Status": "Clouds"},
    {"CityCode": "1850147", "CityName": "Tokyo", "Temp": "8.6", "Status": "Clear"}
  ]
}
```

**Note**: The `Temp` and `Status` fields in the JSON are placeholders and will be replaced with real-time data from the API.

## 🏃 Running the Application

### Option 1: Using Maven Wrapper

```bash
./mvnw spring-boot:run
```

Or on Windows:
```bash
mvnw.cmd spring-boot:run
```

### Option 2: Using Java

```bash
./mvnw clean package
java -jar target/Weather-App-0.0.1-SNAPSHOT.jar
```

### Option 3: Using IDE

Open the project in your IDE (IntelliJ IDEA, Eclipse, etc.) and run the `WeatherAppApplication.java` class.

## 📱 Application Flow

1. **Start**: Navigate to `http://localhost:8080`
2. **Authentication**: Redirected to `/login` page
3. **Auth0 Login**: Click login button to authenticate via Auth0
4. **Authorization**: Auth0 handles user authentication
5. **Callback**: After successful login, redirected back to the application
6. **Weather Dashboard**: Displays at `/` (root) showing:
   - User's name and email
   - Weather information for all configured cities
   - Temperature, weather status, and city details

## 🌐 API Integration

### OpenWeatherMap API

The application uses the OpenWeatherMap Current Weather Data API:

- **Endpoint**: `https://api.openweathermap.org/data/2.5/weather`
- **Parameters**:
  - `id`: City code
  - `appid`: Your API key
  - `units`: metric (for Celsius)

### Caching Strategy

Weather data is cached using Caffeine cache with the following configuration:
- **Cache Duration**: 10 minutes
- **Maximum Size**: 100 entries
- **Purpose**: Reduces API calls and improves response time

## 📁 Project Structure

```
Weather-App/
├── src/
│   ├── main/
│   │   ├── java/com/amongus/Weather/App/
│   │   │   ├── WeatherAppApplication.java       # Main application entry point
│   │   │   ├── config/
│   │   │   │   ├── CachingConfig.java           # Cache configuration
│   │   │   │   └── SecurityConfig.java          # Security & OAuth2 config
│   │   │   ├── controller/
│   │   │   │   ├── LoginController.java         # Login page controller
│   │   │   │   └── WeatherController.java       # Main weather dashboard
│   │   │   ├── model/
│   │   │   │   └── WeatherResponse.java         # Weather API response model
│   │   │   └── service/
│   │   │       ├── CityService.java             # City data service
│   │   │       └── WeatherService.java          # Weather API service
│   │   └── resources/
│   │       ├── application.properties            # App configuration
│   │       ├── cities.json                       # City codes data
│   │       └── templates/
│   │           ├── login.html                    # Login page
│   │           └── weather.html                  # Weather dashboard
│   └── test/
│       └── java/com/amongus/Weather/App/
│           └── WeatherAppApplicationTests.java
├── pom.xml                                        # Maven dependencies
└── README.md                                      # This file
```

## 🔧 Troubleshooting

### Common Issues

1. **Authentication fails**:
   - Verify Auth0 credentials in `application.properties`
   - Check callback URLs in Auth0 dashboard
   - See [TROUBLESHOOTING.md](TROUBLESHOOTING.md) for details

2. **Weather data not loading**:
   - Verify OpenWeatherMap API key is valid
   - Check internet connection
   - Review application logs for API errors

3. **Port 8080 already in use**:
   ```properties
   # Add to application.properties
   server.port=8081
   ```
   Update Auth0 callback URLs accordingly

4. **Build fails**:
   - Ensure Java 21 is installed and configured
   - Clear Maven cache: `mvnw clean`
   - Update dependencies: `mvnw dependency:resolve`

### Logs

Enable debug logging to troubleshoot issues:
```properties
debug=true
logging.level.org.springframework.security=DEBUG
logging.level.com.amongus.Weather.App=DEBUG
```

## 📝 Dependencies

Key dependencies used in this project:

- `spring-boot-starter-web`: Web application support
- `spring-boot-starter-thymeleaf`: Template engine
- `spring-boot-starter-security`: Security framework
- `spring-boot-starter-oauth2-client`: OAuth2 authentication
- `spring-boot-starter-cache`: Caching abstraction
- `caffeine`: High-performance caching library
- `jackson-databind`: JSON processing
- `lombok`: Reduce boilerplate code

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is created for educational purposes.

## 👥 Authors

- Your Name/Team

## 🙏 Acknowledgments

- [OpenWeatherMap](https://openweathermap.org/) for weather data API
- [Auth0](https://auth0.com/) for authentication services
- [Spring Boot](https://spring.io/projects/spring-boot) framework

---

**Note**: Remember to keep your API keys and secrets secure. Never commit sensitive credentials to version control. Use environment variables or external configuration for production deployments.


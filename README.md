# Device Management

This application is a REST API for managing devices. It allows users to perform CRUD operations and search for devices by brand.

## Table of Contents
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Configuration](#configuration)
    - [Database Configuration](#database-configuration)
    - [Swagger Configuration](#swagger-configuration)
    - [Security Configuration](#security-configuration)

## Features

- CRUD operations for devices
- Search devices by brand
- Detailed API documentation with Swagger
- Exception handling
- Security configuration using Spring Security

## Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/reddyzhub/devicemanagement.git
    cd devicemanagement
    ```

2. Build the project:
    ```sh
    ./mvnw clean install
    ```

3. Run the application:
    ```sh
    ./mvnw spring-boot:run
    ```

## Usage

Once the application is running, you can access the API at `http://localhost:8080`.

Swagger UI is available at `http://localhost:8080/swagger-ui.html` for exploring the API.

H2 Database Console is available at `http://localhost:8080/h2-console` for database management.

## API Endpoints

There are endpoints

| Method | Path          | Descriptions                                                       |
|--------|---------------|--------------------------------------------------------------------|
| GET    | /devices      | List all existing Devices                                          |   
| GET    | /devices/{id} | Get device by identifier                                           |   
| POST   | /devices      | Adds a new device. Id and created date are automatically generated |
| PUT    | /devices/{id} | Full update of the Device                                          | 
| PATCH |  /devices/{id} | Partial update of the Device                                       |
| DELETE | /devices/{id} | Delete the device with the given Id                                |
| GET | /devices/search/brand/{brand}| Search the device by brand |


### Device Management

- **Add a new device**
    ```http
    POST /devices
    ```
  Request Body:
    ```json
    {
        "name": "Device Name",
        "brand": "Device Brand"
    }
    ```
  Response:
    ```json
    {
        "id": 1,
        "name": "Device Name",
        "brand": "Device Brand",
        "creationTime": "2023-07-09T12:34:56"
    }
    ```

- **Get a device by ID**
    ```http
    GET /devices/{id}
    ```
  Response:
    ```json
    {
        "id": 1,
        "name": "Device Name",
        "brand": "Device Brand",
        "creationTime": "2023-07-09T12:34:56"
    }
    ```

- **Get all devices**
    ```http
    GET /devices
    ```
  Response:
    ```json
    [
        {
            "id": 1,
            "name": "Device Name",
            "brand": "Device Brand",
            "creationTime": "2023-07-09T12:34:56"
        },
        ...
    ]
    ```

- **Update a device**
    ```http
    PUT /devices/{id}
    ```
  Request Body:
    ```json
    {
        "name": "Updated Device Name",
        "brand": "Updated Device Brand"
    }
    ```
  Response:
    ```json
    {
        "id": 1,
        "name": "Updated Device Name",
        "brand": "Updated Device Brand",
        "creationTime": "2023-07-09T12:34:56"
    }
    ```

- **Partially update a device**
    ```http
    PATCH /devices/{id}
    ```
  Request Body:
    ```json
    {
        "name": "Partially Updated Device Name"
    }
    ```
  Response:
    ```json
    {
        "id": 1,
        "name": "Partially Updated Device Name",
        "brand": "Device Brand",
        "creationTime": "2023-07-09T12:34:56"
    }
    ```

- **Delete a device**
    ```http
    DELETE /devices/{id}
    ```
  Response: `204 No Content`

- **Search devices by brand**
    ```http
    GET /devices/search/brand/{brand}
    ```
  Response:
    ```json
    [
        {
            "id": 1,
            "name": "Device Name",
            "brand": "Device Brand",
            "creationTime": "2023-07-09T12:34:56"
        },
        ...
    ]
    ```

## Configuration

### Database Configuration

The application uses an in-memory H2 database. The configuration can be found in the `application.properties` file:

```properties
spring.datasource.url=jdbc:h2:mem:devicedb
spring.datasource.username=sa
spring.datasource.password=password
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### Swagger Configuration

Swagger is enabled for API documentation and can be accessed at `/swagger-ui.html`.

### Security Configuration

The application uses Spring Security for securing the endpoints. The configuration is defined in the `SecurityConfiguration` class:

```java
package com.example.devicemanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(antMatcher("/swagger-ui/**"),
                                    antMatcher("/swagger-ui.html"),
                                    antMatcher("/v3/**"),
                                    antMatcher("/h2-console/**")).permitAll()
                            .requestMatchers(antMatcher(HttpMethod.GET, "/devices")).permitAll()
                            .requestMatchers(antMatcher(HttpMethod.GET, "/devices/**")).permitAll()
                            .requestMatchers(antMatcher(HttpMethod.PUT, "/devices/**")).permitAll()
                            .requestMatchers(antMatcher(HttpMethod.POST, "/devices/**")).permitAll()
                            .requestMatchers(antMatcher(HttpMethod.DELETE, "/devices/**")).permitAll()
                            .anyRequest().authenticated();
                })
                .headers(h -> h.frameOptions(
                        HeadersConfigurer.FrameOptionsConfig::sameOrigin)) // This so embedded frames in h2-console are working
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }
}
```

### Explanation

- **CSRF Protection Disabled**: CSRF protection is disabled for simplicity. In a production environment, it is recommended to enable CSRF protection.
- **Public Endpoints**: The Swagger UI, H2 console, and device-related endpoints are publicly accessible.
- **Authenticated Endpoints**: Any other request requires authentication.
- **Frame Options**: The `frameOptions` are configured to allow the H2 console to be embedded in a frame.
- **HTTP Basic Authentication**: Basic authentication is configured to secure the application.
- **Stateless Session**: The session management is set to stateless, meaning that the server does not maintain any session information.

### Note

For a production environment, it is recommended to use stronger authentication mechanisms such as OAuth2 or JWT and to secure sensitive endpoints appropriately.

### Optional Password Encoder (Commented Out)

The `PasswordEncoder` bean is commented out in the configuration. If you need to encode passwords, you can uncomment and configure it.

```java
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(12);
//    }
```

This setup provides a basic but secure foundation for managing authentication and authorization within the application. Feel free to adjust the security settings as per your requirements.
# money-button

This project demonstrates a Spring Boot 3.3 application.

## Build

```
mvn clean package
```

## Docker

Build and run the Docker container:

```
docker build -t money-button .
docker run -p 8080:8080 money-button
```

The application exposes `GET /actuator/health` for health checks.

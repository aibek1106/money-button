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

## Machine Learning

Run `python train_lightgbm.py` to train a simple LightGBM model and export it to
`src/main/resources/model.onnx`. The repository does not include this binary
file, so you must generate it yourself before running the application or tests.
The application uses ONNX Runtime to load this model and perform predictions via
`OnnxModelService`. `MlScorerService` wraps this component and publishes an
`AlertEvent` whenever a score is at least `0.70`.

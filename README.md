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

## Alert Pipeline

`AlertEvent`s are forwarded to a Redis-backed `AlertBus`. `TelegramNotifier`
subscribes to this channel and sends a Markdown message via Telegram's
`sendMessage` API using OkHttp. A chat configured with `secrets.tgChat` receives
an alert within seconds of the scoring call.

## Feedback & Retraining

`POST /api/v1/feedback` accepts a JSON body with token features and an `outcome`
flag. Rows are stored in ClickHouse and a nightly job exports new labels to a
CSV file in the MinIO bucket configured via `secrets.*`. When at least 200 new
labels are exported the job logs `retrain scheduled` to standard output.

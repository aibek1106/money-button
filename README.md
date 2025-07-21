# money-button

This project demonstrates a Spring Boot 3.3 application used for scanning Solana tokens.

## Prerequisites
- **JDK 21**
- **Maven 3.9**
- **Docker** (for containerised execution)

## Environment Variables
Set the following variables before running locally or in Docker:
- `BIRDEYE_KEY`
- `HELIUS_KEY`
- `TG_TOKEN`
- `TG_CHAT`
- `CLICKHOUSE_ENABLED` (optional, default true)

## Local Build & Run
```bash
mvn clean package
mvn spring-boot:run
```
The application exposes `GET /actuator/health` and `GET /actuator/prometheus` endpoints.

## Docker
Build and run via Docker:
```bash
docker build -t money-button .
docker run -p 8080:8080 \
  -e BIRDEYE_KEY=$BIRDEYE_KEY \
  -e HELIUS_KEY=$HELIUS_KEY \
  -e TG_TOKEN=$TG_TOKEN \
  -e TG_CHAT=$TG_CHAT \
  ghcr.io/you/solana-scanner
```

## Kubernetes
For k3s/k8s you can deploy with Helm:
```bash
helm install scanner chart/ \
  --set secrets.birdeyeKey=$BIRDEYE_KEY \
  --set secrets.heliusKey=$HELIUS_KEY \
  --set secrets.tgToken=$TG_TOKEN \
  --set secrets.tgChat=$TG_CHAT
```

## Metrics & OpenTelemetry
Metrics are exposed on `/actuator/prometheus`. To enable OTEL tracing set standard
OpenTelemetry environment variables such as `OTEL_EXPORTER_OTLP_ENDPOINT` when running.

## Feedback API
`POST /api/v1/feedback` accepts a JSON body:
```json
{
  "features": {"values": [0.1,0.2,0.3,0.4,0.5]},
  "outcome": true
}
```
Entries are persisted to ClickHouse and exported nightly for potential retraining.

# Monitoring Demo

## Overview
Spring Boot example project with 
- some example REST endpoints
- in-memory repository
- some Spring Boot Actuator endpoints enabled
- standard metrics collection via Micrometer https://micrometer.io/ for Prometheus https://prometheus.io/
- custom metrics example via Micrometer: a custom meter of type `Counter` called `exampleData_created_total`
- structured logging via Logstash Logback Encoder https://github.com/logstash/logstash-logback-encoder
- log management via Promtail agent for Loki https://grafana.com/oss/loki/
- monitoring and logging dashboards for Grafana https://grafana.com/
- distributed tracing via Spring Cloud Sleuth https://spring.io/projects/spring-cloud-sleuth for Jaeger https://www.jaegertracing.io/
- TODO planned: custom tracing spans example via Spring Cloud Sleuth

## Example Endpoints
The application when started locally is running on port 8080
and the following example endpoints are available

### Read all example data
GET http://localhost:8080/example
Example response payload

`[
     {
         "name": "answer",
         "value": 42
     },
     {
         "name": "test",
         "value": 123
     }
 ]`

### Read example data by name
GET http://localhost:8080/example/{name}
For example, http://localhost:8080/example/answer gets the response payload

`{
     "name": "answer",
     "value": 42
 }`

### Write example data
POST http://localhost:8080/example
Example request payload

`{
       "name": "test",
       "value": 123
}`

## Actuator Endpoints
The following Actuator endpoints are enabled

### Info
GET http://localhost:8080/actuator/info

### Health
GET http://localhost:8080/actuator/health

### Prometheus Metrics
GET http://localhost:8080/actuator/prometheus

## Logfiles
Logs are written as text to STDOUT 
and as structured JSON to `./logs/monitoring.log`

## Starting Monitoring and Logging Tools
Monitoring and logging tools can be started in path `./docker` via docker-compose

`docker-compose up`

The following tools are available:

### Prometheus
Prometheus can be reached via http://localhost:9090

Incorporating a prometheus.yml configuration 
scraping from `targets: ['host.docker.internal:8080']`
with `metrics_path: '/actuator/prometheus'`.

### Promtail
This agent is incorporating a promtail-docker-config.yml
scraping from a volume mounted under /var/log/monitoring.log
to Loki endpoint http://monitoring-loki:3100/loki/api/v1/push

### Loki
Loki is currently left to default configuration using the filesystem
for both index and chunks storage.

Loki is running on http://localhost:3100 
with e.g. http://localhost:3100/metrics being an available endpoint
and http://localhost:3100/loki/api/v1/query?query=%7Bjob=%22monitoring-demo%22%7D being a valid query.

### Grafana
Grafana can be reached via http://localhost:3000

It is incorporating a datasource.yaml configuration
with the following datasources defined:
- monitoring-prometheus-datasource of type prometheus 
pointing at Prometheus via `url: http://monitoring-prometheus:9090`
- monitoring-loki-datasource of type loki
pointing at Loki via `url: http://monitoring-loki:3100`
- monitoring-loki-as-prometheus-datasource of type prometheus
pointing at Loki via `url: http://monitoring-loki:3100/loki`

An example dashboard named "Experiment" is already configured and loaded
using request metrics from our example app provided via Prometheus
as well as log entries and log metrics provided via Loki.

### Jaeger
Jaeger UI can be reached via http://localhost:16686

Started using jaegertracing/all-in-one image,
Jaeger collector is configured to expose a Zipkin compatible endpoint under http://localhost:9411/.

POST http://localhost:9411/api/v2/spans uploads a list of spans encoded per content-type, for example json.
See also https://zipkin.io/zipkin-api/#/default/post_spans

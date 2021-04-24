# Inmemory Key Value Application
In-memory key-value store HTTP API Service

## Build and run

With JDK11+
```bash
cd Inmemory-Key-value-Java
mvn package
java -jar target/inmem-key-value.jar
```

## API usage examples

```
#Setting the key value pair
curl -X POST -H "Content-Type: application/json" -d '{"key" : "abc-1","value" : "xyz"}' http://localhost:8080/set

#Getting the key value pair
curl -X GET http://localhost:8080/get/abc-1
{"key":"abc-1","value":"xyz"}

#Search : Get keys with matching prefix 
curl -X GET http://localhost:8080/search\?prefix\=abc
["abc-1"]

#Search : Get keys with matching suffix
curl -X GET http://localhost:8080/search\?suffix\=-1 
["abc-1"]

```

## Application Prometheus Metrics

```
curl -s -X GET http://localhost:8080/metrics/application


# TYPE application_GET_API_COUNT_total counter
# HELP application_GET_API_COUNT_total 
application_GET_API_COUNT_total 1
# TYPE application_GET_API_LATENCY_total counter
# HELP application_GET_API_LATENCY_total 
application_GET_API_LATENCY_total 1
# TYPE application_GET_API_LATENCY_elapsedTime_seconds gauge
application_GET_API_LATENCY_elapsedTime_seconds 3.035E-4
# TYPE application_NO_OF_KEYS_IN_DB_total counter
# HELP application_NO_OF_KEYS_IN_DB_total 
application_NO_OF_KEYS_IN_DB_total 1
# TYPE application_SEARCH_API_HIT_COUNT_total counter
# HELP application_SEARCH_API_HIT_COUNT_total 
application_SEARCH_API_HIT_COUNT_total 2
# TYPE application_SEARCH_API_LATENCY_total counter
# HELP application_SEARCH_API_LATENCY_total 
application_SEARCH_API_LATENCY_total 2
# TYPE application_SEARCH_API_LATENCY_elapsedTime_seconds gauge
application_SEARCH_API_LATENCY_elapsedTime_seconds 0.0030948
# TYPE application_SET_API_HIT_COUNT_total counter
# HELP application_SET_API_HIT_COUNT_total 
application_SET_API_HIT_COUNT_total 1
# TYPE application_SET_API_LATENCY_total counter
# HELP application_SET_API_LATENCY_total 
application_SET_API_LATENCY_total 1
# TYPE application_SET_API_LATENCY_elapsedTime_seconds gauge
application_SET_API_LATENCY_elapsedTime_seconds 0.0114564
# TYPE application_httpResponse_total counter
# HELP application_httpResponse_total Counts the number of HTTP responses in each status category (1xx, 2xx, etc.)
application_httpResponse_total{range="1xx"} 0
application_httpResponse_total{range="2xx"} 3
application_httpResponse_total{range="3xx"} 0
application_httpResponse_total{range="4xx"} 0
application_httpResponse_total{range="5xx"} 0

```

## Build the Docker Image

```
cd Inmemory-Key-value-Java
docker build -t inmem-key-value .
```

## Start the application with Docker

```
docker run --rm -p 8080:8080 inmem-key-value:latest
```

## Terraform scripts for provisioning OKE
Added terraform scripts for provisioning kubernetes cluster(OKE) in OCI. 
```
cd Terraform
terraform --version            # > 0.12 version
terraform init                 #intialise terraform

#Set required TF_VARs for oci env
terraform plan                 #Check what all reources  terraform is creating
terraform apply                 #apply for creating resources 
```


## Deploy the application to Kubernetes

```
cd Kubernetes
kubectl cluster-info                         # Verify which cluster
kubectl get pods                             # Verify connectivity to cluster
kubectl create -f ./deployment.yaml -f ./service.yaml -f ./ingress.yaml                   # Deploy application
kubectl get pods                             # Wait for quickstart pod to be RUNNING
kubectl get service inmem-key-value          # Verify deployed service
```

```
#For Deleting deployment
kubectl delete ./deployment.yaml -f ./service.yaml -f ./ingress.yaml
```


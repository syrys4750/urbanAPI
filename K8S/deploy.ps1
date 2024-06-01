#!/bin/bash
kubectl create namespace urban

kubectl apply -f configmaps/apis-url-configmap.yaml
kubectl apply -f configmaps/persistence-url-configmap.yaml

kubectl apply -f secrets/auth-api-secret.yaml
kubectl apply -f secrets/municipal-api-secret.yaml
kubectl apply -f secrets/mysql-secret.yaml

kubectl apply -f persistentVolumes
kubectl apply -f persistentVolumeClaims

# Desplegar todos los archivos de persistencia
kubectl apply -f deployments/persistence/mongo-aggregate-deployment.yaml
kubectl apply -f deployments/persistence/mongo-mobility-deployment.yaml
kubectl apply -f deployments/persistence/mongo-pollution-deployment.yaml
kubectl apply -f deployments/persistence/mysql-mobility-deployment.yaml
kubectl apply -f deployments/persistence/mysql-pollution-deployment.yaml
sleep 30
# Desplegar todos los archivos de nosql
kubectl apply -f deployments/urban/aggregated-nosql-repository-deployment.yaml
kubectl apply -f deployments/urban/mobility-nosql-repository.yaml
kubectl apply -f deployments/urban/pollution-nosql-repository-deployment.yaml
sleep 20
# Desplegar todos los archivos de sql
kubectl apply -f deployments/urban/mobility-sql-repository-deployment.yaml
kubectl apply -f deployments/urban/pollution-sql-repository-deployment.yaml
sleep 20
# Desplegar todos los archivos de api
kubectl apply -f deployments/urban/auth-api-deployment.yaml
kubectl apply -f deployments/urban/mobility-api-deployment.yaml
kubectl apply -f deployments/urban/pollution-api-deployment.yaml
kubectl apply -f deployments/urban/municipal-api-deployment.yaml

kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/baremetal/deploy.yaml

kubectl -n ingress-nginx annotate ingressclasses nginx ingressclass.kubernetes.io/is-default-class="true"

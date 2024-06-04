#!/bin/bash

# Crear namespace urban
kubectl create namespace urban

# Aplicar configuraciones (configmaps, secrets, volúmenes)
kubectl apply -f ./configmaps
kubectl apply -f ./secrets
kubectl apply -f ./persistentVolumes
kubectl apply -f ./persistentVolumeClaims

# Desplegar contenedores de persistencia (MySQL y MongoDB)
kubectl apply -f ./deployments/persistence/mongo-aggregate-deployment.yaml
kubectl apply -f ./deployments/persistence/mongo-mobility-deployment.yaml
kubectl apply -f ./deployments/persistence/mongo-pollution-deployment.yaml
kubectl apply -f ./deployments/persistence/mysql-mobility-deployment.yaml
kubectl apply -f ./deployments/persistence/mysql-pollution-deployment.yaml
sleep 30

# Desplegar contenedores NoSQL
kubectl apply -f ./deployments/urban/aggregated-nosql-repository-deployment.yaml
kubectl apply -f ./deployments/urban/mobility-nosql-repository.yaml
kubectl apply -f ./deployments/urban/pollution-nosql-repository-deployment.yaml
sleep 20

# Desplegar contenedores SQL
kubectl apply -f ./deployments/urban/mobility-sql-repository-deployment.yaml
kubectl apply -f ./deployments/urban/pollution-sql-repository-deployment.yaml
sleep 20

# Desplegar APIs
kubectl apply -f ./deployments/urban/auth-api-deployment.yaml
kubectl apply -f ./deployments/urban/mobility-api-deployment.yaml
kubectl apply -f ./deployments/urban/pollution-api-deployment.yaml
kubectl apply -f ./deployments/urban/municipal-api-deployment.yaml
sleep 20

# Configurar Ingress
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/baremetal/deploy.yaml
kubectl -n ingress-nginx annotate ingressclasses nginx ingressclass.kubernetes.io/is-default-class="true"
sleep 10
kubectl apply -f ./deployments/ingress/urban-ingress.yaml

# Cronjobs
kubectl apply -f ./cronjobs

# Dashboard
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.7.0/aio/deploy/recommended.yaml
kubectl apply -f https://routerdi1315.uv.es/blob/dashboard-k8s-sa-user.yaml
sleep 5
token=$(kubectl get secret admin-user -n kubernetes-dashboard -o jsonpath="{.data.token}" | base64 -d)
echo "La clave de acceso al dashboard es la siguiente: $token"

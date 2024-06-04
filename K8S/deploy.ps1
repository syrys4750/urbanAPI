#!/bin/bash
kubectl create namespace urban

# Apply configs (configmaps, secrets, volumes)
kubectl apply -f ./configmaps
kubectl apply -f ./secrets
kubectl apply -f ./persistentVolumes
kubectl apply -f ./persistentVolumeClaims

# Deploy persistence containers (MySQL and MonogDB)
kubectl apply -f ./deployments/persistence/mongo-aggregate-deployment.yaml
kubectl apply -f ./deployments/persistence/mongo-mobility-deployment.yaml
kubectl apply -f ./deployments/persistence/mongo-pollution-deployment.yaml
kubectl apply -f ./deployments/persistence/mysql-mobility-deployment.yaml
kubectl apply -f ./deployments/persistence/mysql-pollution-deployment.yaml
sleep 30

# Deploy NoSQL containers
kubectl apply -f ./deployments/urban/aggregated-nosql-repository-deployment.yaml
kubectl apply -f ./deployments/urban/mobility-nosql-repository.yaml
kubectl apply -f ./deployments/urban/pollution-nosql-repository-deployment.yaml
sleep 20

# Deploy SQL containers
kubectl apply -f ./deployments/urban/mobility-sql-repository-deployment.yaml
kubectl apply -f ./deployments/urban/pollution-sql-repository-deployment.yaml
sleep 20

# Deploy APIs
kubectl apply -f ./deployments/urban/auth-api-deployment.yaml
kubectl apply -f ./deployments/urban/mobility-api-deployment.yaml
kubectl apply -f ./deployments/urban/pollution-api-deployment.yaml
kubectl apply -f ./deployments/urban/municipal-api-deployment.yaml
sleep 20

# Ingress config
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/baremetal/deploy.yaml
kubectl -n ingress-nginx annotate ingressclasses nginx ingressclass.kubernetes.io/is-default-class="true"
sleep 100
kubectl apply -f ./deployments/ingress/urban-ingress.yaml

# Cronjobs
kubectl apply -f ./cronjobs

# Dashboard
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.7.0/aio/deploy/recommended.yaml
kubectl apply -f https://routerdi1315.uv.es/blob/dashboard-k8s-sa-user.yaml
sleep 5
$token = kubectl get secret admin-user -n kubernetes-dashboard -o jsonpath="{.data.token}" | base64 -d
Write-Output "La clave de acceso al dashboard es la siguiente: $token"
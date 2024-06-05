#!/bin/bash

# Función para comprobar si los pods están listos en el namespace urban
function wait_for_pods_ready {
    echo "Comprobando el estado de los pods en el namespace urban..."
    while true; do
        # Obtener el estado de los pods en el namespace urban
        pod_status=$(kubectl get pods -n urban --no-headers)

        # Contar el número de pods en estado Running/Completed
        num_ready=$(echo "$pod_status" | grep -c 'Running\|Completed')

        # Obtener el número total de pods esperados
        num_expected=$(echo "$pod_status" | wc -l)

        # Si todos los pods están listos, salir del bucle
        if [ "$num_ready" -eq "$num_expected" ]; then
            echo "Todos los pods en el namespace urban están listos."
            break
        fi

        # Esperar 10 segundos antes de volver a comprobar
        sleep 10
    done
}

# Crear el namespace urban
kubectl create namespace urban

# Aplicar configuraciones (configmaps, secrets, volumes)
kubectl apply -f ./configmaps
kubectl apply -f ./secrets
kubectl apply -f ./persistentVolumes
kubectl apply -f ./persistentVolumeClaims

# Desplegar contenedores de persistencia (MySQL y MonogDB)
kubectl apply -f ./deployments/persistence/mongo-aggregate-deployment.yaml
kubectl apply -f ./deployments/persistence/mongo-mobility-deployment.yaml
kubectl apply -f ./deployments/persistence/mongo-pollution-deployment.yaml
kubectl apply -f ./deployments/persistence/mysql-mobility-deployment.yaml
kubectl apply -f ./deployments/persistence/mysql-pollution-deployment.yaml

# Esperar a que los pods de persistencia estén listos
wait_for_pods_ready

# Desplegar contenedores NoSQL
kubectl apply -f ./deployments/urban/aggregated-nosql-repository-deployment.yaml
kubectl apply -f ./deployments/urban/mobility-nosql-repository.yaml
kubectl apply -f ./deployments/urban/pollution-nosql-repository-deployment.yaml

# Esperar a que los pods NoSQL estén listos
wait_for_pods_ready

# Desplegar contenedores SQL
kubectl apply -f ./deployments/urban/mobility-sql-repository-deployment.yaml
kubectl apply -f ./deployments/urban/pollution-sql-repository-deployment.yaml

# Esperar a que los pods SQL estén listos
wait_for_pods_ready

# Desplegar APIs
kubectl apply -f ./deployments/urban/auth-api-deployment.yaml
kubectl apply -f ./deployments/urban/mobility-api-deployment.yaml
kubectl apply -f ./deployments/urban/pollution-api-deployment.yaml
kubectl apply -f ./deployments/urban/municipal-api-deployment.yaml

# Esperar a que los pods de las APIs estén listos
wait_for_pods_ready

# Desplegar Ingress Controller
echo "Desplegando el controlador de Ingress..."
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/baremetal/deploy.yaml
kubectl -n ingress-nginx annotate ingressclasses nginx ingressclass.kubernetes.io/is-default-class="true"

# Esperar a que el controlador de Ingress esté listo
# Se utiliza un sleep ya que la comprobación del estado de los pods del controlador de Ingress no es trivial.
# Dado que el controlador de Ingress consta de tres pods en el momento que está preparado, 
# con dos en estado "Completed" y uno en estado "Running", y debido a que puede cambiar según la versión
# se ha considerado implementar un sleep
sleep 150

# Desplegar la configuración de Ingress
echo "Desplegando la configuración de Ingress..."
kubectl apply -f ./deployments/ingress/urban-ingress.yaml

# Desplegar Cronjobs
echo "Desplegando Cronjobs..."
kubectl apply -f ./cronjobs

# Desplegar el dashboard
echo "Desplegando el dashboard..."
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.7.0/aio/deploy/recommended.yaml
kubectl apply -f https://routerdi1315.uv.es/blob/dashboard-k8s-sa-user.yaml

# Esperar a que el dashboard esté listo
sleep 20

# Obtener el token de acceso al dashboard
token=$(kubectl get secret admin-user -n kubernetes-dashboard -o jsonpath="{.data.token}" | base64 -d)
echo "La clave de acceso al dashboard es la siguiente: $token"

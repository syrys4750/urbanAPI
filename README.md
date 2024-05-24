# UrbanAPI Project

## Overview

This project involves creating a set of APIs for managing bicycle parking and pollution monitoring stations in a city. It is a joint project between DBCDS, PRNRD, and CN for the 23/24 academic year.

## APIs

### 1. Bicycle API

This API manages bicycle parking stations and their operations.

#### Endpoints

- **Add Parking**: Adds a new bicycle parking station.
  - Endpoint: `/aparcamiento`
  - Method: `POST`
  - Role: `admin`
- **Delete Parking**: Deletes a bicycle parking station by ID.
  - Endpoint: `/aparcamiento/{id}`
  - Method: `DELETE`
  - Role: `admin`
- **Edit Parking**: Edits details of a bicycle parking station.
  - Endpoint: `/aparcamiento/{id}`
  - Method: `PUT`
  - Role: `admin`
- **List Parkings**: Retrieves a list of all bicycle parking stations.
  - Endpoint: `/aparcamientos`
  - Method: `GET`
- **Log Event**: Logs an event such as parking or renting a bicycle.
  - Endpoint: `/evento/{id}`
  - Method: `POST`
  - Role: `aparcamiento`
- **Check Parking Status**: Gets the current status of a bicycle parking station.
  - Endpoint: `/aparcamiento/{id}/status`
  - Method: `GET`
- **Check Parking Status by Interval**: Gets the status changes of a bicycle parking station over a specified time interval.
  - Endpoint: `/aparcamiento/{id}/status?from=X&to=Y`
  - Method: `GET`
- **Top 10 Parkings**: Gets the top 10 bicycle parking stations with the most available bikes at a given time.

#### Data Model

- **Relational Model**: For storing parking station identifiers and locations.
- **Non-Relational Model**: For storing the operational status of parking stations.
  ```json
  {
    "id": 1,
    "operation": "rent",
    "bikesAvailable": 50,
    "freeParkingSpots": 20,
    "timestamp": "2022-03-23T14:15:30.000Z"
  }
  ```

### 2. Pollution API

This API manages pollution monitoring stations and their data.

#### Endpoints

- **Add Station**: Adds a new pollution monitoring station.
  - Endpoint: `/estacion`
  - Method: `POST`
  - Role: `admin`
- **Delete Station**: Deletes a pollution monitoring station by ID.
  - Endpoint: `/estacion/{id}`
  - Method: `DELETE`
  - Role: `admin`
- **Edit Station**: Edits details of a pollution monitoring station.
  - Endpoint: `/estacion/{id}`
  - Method: `PUT`
  - Role: `admin`
- **List Stations**: Retrieves a list of all pollution monitoring stations.
  - Endpoint: `/estaciones`
  - Method: `GET`
- **Log Measurement**: Logs a pollution measurement from a station.
  - Endpoint: `/estacion/{id}`
  - Method: `POST`
  - Role: `estacion`
- **Check Station Status**: Gets the latest reading from a pollution monitoring station.
  - Endpoint: `/estacion/{id}/status`
  - Method: `GET`
- **Check Station Status by Interval**: Gets readings from a pollution monitoring station over a specified time interval.
  - Endpoint: `/estacion/{id}/status?from=X&to=Y`
  - Method: `GET`

#### Data Model

- **Relational Model**: For storing station identifiers and locations.
- **Non-Relational Model**: For storing pollution measurements.
  ```json
  {
    "id": 1,
    "timeStamp": "2024-03-01T14:30:57Z",
    "nitricOxides": 10.5,
    "nitrogenDioxides": 5.2,
    "VOCs_NMHC": 6.8,
    "PM2_5": 7.1
  }
  ```

### 3. City API

This API provides additional functionalities integrating the bicycle and pollution APIs.

#### Endpoints

- **Nearest Parking**: Gets the nearest parking station with available bikes to a given location.
  - Endpoint: `/aparcamientoCercano?lat=X&lon=Y`
  - Method: `GET`
- **Aggregate Data**: Aggregates data from bicycle and pollution APIs and stores it in a non-relational database.
  - Endpoint: `/aggregateData`
  - Method: `GET`
  - Role: `servicio`
- **Get Aggregated Data**: Retrieves the latest aggregated data.
  - Endpoint: `/aggregatedData`
  - Method: `GET`
- **Access Admin Methods**: Provides access to admin methods from the bicycle and pollution APIs.
  - Endpoint: `/estacion`, `/estacion/{id}`, `/aparcamiento`, `/aparcamiento/{id}`
  - Method: `POST`, `DELETE`
  - Role: `admin`

#### Data Model

- **Aggregated Data**: Example document storing aggregated data.
  ```json
  {
    "timeStamp": "2024-04-01T12:00:00Z",
    "aggregatedData": [
      {
        "id": 1,
        "average_bikesAvailable": 3.5,
        "air_quality": {
          "nitricOxides": 10.5,
          "nitrogenDioxides": 5.2,
          "VOCs_NMHC": 6.8,
          "PM2_5": 7.1
        }
      },
      {
        "id": 2,
        "average_bikesAvailable": 10.6,
        "air_quality": {
          "nitricOxides": 12.5,
          "nitrogenDioxides": 7.2,
          "VOCs_NMHC": 16.8,
          "PM2_5": 1.1
        }
      }
    ]
  }
  ```

## General Schema

## Data Persistence

- **Relational Database**: Used for storing identifiers and locations of parking stations and sensors, as well as user management data.
- **Non-Relational Database (MongoDB)**: Used for storing operational data from parking stations, sensor data, and aggregated data.

## Development and Deployment

- **Components**: The APIs are developed using Spring Boot with profiles for standalone execution.
- **Cloud Deployment**: The application is deployed on Kubernetes. Required YAML files, scripts, and configuration files for deployments, services, ingress, secrets, configmaps, cronjobs, etc., are included.

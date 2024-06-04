# API bicletas repositorios
http POST http://localhost:8082/api/v1/aparcamiento id=1 direction="124 Main St" bikesCapacity=20 latitude=40.712776 longitude=-74.005974
http DELETE http://localhost:8082/api/v1/aparcamiento/1
http PUT http://localhost:8082/api/v1/aparcamiento/5 direction="456 Elm St" bikesCapacity=25 latitude=40.712776 longitude=-74.005974
http GET http://localhost:8082/api/v1/aparcamientos
http POST http://localhost:8083/api/v1/evento/2 idParking=2 operation="parking" bikesAvailable=15 freeParkingSpots=5 timestamp="2024-06-04T10:00:00"
http GET http://localhost:8083/api/v1/aparcamiento/1/status
http GET http://localhost:8083/api/v1/aparcamiento/1/status?from="2022-06-01T00:00:00""&"to="2024-06-04T23:59:59"
http GET http://localhost:8083/api/v1/top10
# API polucion repositorios
http POST http://localhost:8080/api/v1/estacion  direction="124 Main St" latitude=40.712776 longitude=-74.005974
http DELETE http://localhost:8080/api/v1/estacion/12
http PUT http://localhost:8080/api/v1/estacion/2 id=3 direction="125 Main St" latitude=40.712776 longitude=-74.005974
http GET http://localhost:8080/api/v1/estaciones
http POST http://localhost:8081/api/v1/estacion/1 idStation=1 timestamp="2022-06-01T00:00:00Z" nitricOxides=10.1 nitrogenDioxides=11.2 vocs_nmhc=9.1 pm2_5=8.3
# API Agregados repositorios
http GET http://localhost:8084/api/v1/aggregatedData 
# post con el swagger

# API POLUCION
http POST http://localhost:8085/api/v1/estacion direction="124 Main St" bikesCapacity=20 latitude=40.712776 longitude=-74.005974 -A bearer -a eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpc3MiOiJzZXJzb21vbiIsImV4cCI6NTMxNzUwODA2OX0.fN9ffe_Vbw3rUrOWMSeo3nULXrUBT8VSJJiGUBiiRXE

http POST http://localhost:8085/api/v1/estacion/14 direction="124 Main St" idStation=14 timestamp="2022-06-01T00:00:00Z" nitricOxides=10.1 nitrogenDioxides=11.2 vocs_nmhc=9.1 pm2_5=8.3 -A bearer -a # token del paso anterior
http DELETE http://localhost:8085/api/v1/estacion/14 -A bearer -a eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpc3MiOiJzZXJzb21vbiIsImV4cCI6NTMxNzUwODA2OX0.fN9ffe_Vbw3rUrOWMSeo3nULXrUBT8VSJJiGUBiiRXE
http GET http://localhost:8085/api/v1/estaciones
http GET http://localhost:8085/api/v1/estacion/15/status
http GET http://localhost:8085/api/v1/estacion/1/status?from="2022-04-01T00:00:00Z""&"to="2026-06-01T00:00:00Z"

# API bicicletas
http POST http://localhost:8086/api/v1/aparcamiento id=1 direction="124 Main St" bikesCapacity=20 latitude=40.712776 longitude=-74.005974
http DELETE http://localhost:8086/api/v1/aparcamiento/16 -A bearer -a eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpc3MiOiJzZXJzb21vbiIsImV4cCI6NTMxNzUwODA2OX0.fN9ffe_Vbw3rUrOWMSeo3nULXrUBT8VSJJiGUBiiRXE
http GET http://localhost:8086/api/v1/aparcamientos -A bearer -a eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpc3MiOiJzZXJzb21vbiIsImV4cCI6NTMxNzUwODA2OX0.fN9ffe_Vbw3rUrOWMSeo3nULXrUBT8VSJJiGUBiiRXE
http POST http://localhost:8086/api/v1/evento/13 idParking=13 operation=rent bikesAvailable=23 freeParkingSpots=12 timestamp=2025-06-01T00:00:00 -A bearer -a eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfUEFSS0lORyIsImlzcyI6InNlcnNvbW9uIiwiZXhwIjo1MzE3NTA4MDY5fQ.0jxmem_alnxHCeOB2S8f0vxyYfXF9uLAvp4kHRkorWQ
http GET http://localhost:8086/api/v1/aparcamiento/13/status
http GET http://localhost:8086/api/v1/aparcamiento/13/status?from="2021-06-01T00:00:00Z""&"to="2026-06-01T00:00:00Z"
# API Ayuntamiento
http GET http://localhost:8087/api/v1/aparcamientoCercano?lat=2&lon=2
http GET http://localhost:8087/api/v1/aggregateData -A bearer -a eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfU0VSVklDRSIsImlzcyI6InNlcnNvbW9uIiwiZXhwIjo1MzE3NTA4MDY5fQ.9fUKXlIMsZl0BjGK0jPV5eq9PacQFgOuwM84Lak6Wi0
http GET http://localhost:8087/api/v1/aggregatedData
http POST http://localhost:8087/api/v1/estacion direction="124 Main St" bikesCapacity=20 latitude=40.712776 longitude=-74.005974 -A bearer -a eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpc3MiOiJzZXJzb21vbiIsImV4cCI6NTMxNzUwODA2OX0.fN9ffe_Vbw3rUrOWMSeo3nULXrUBT8VSJJiGUBiiRXE
http POST http://localhost:8087/api/v1/aparcamiento direction="128 Main St" bikesCapacity=20 latitude=40.712776 longitude=-74.005974  -A bearer -a eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpc3MiOiJzZXJzb21vbiIsImV4cCI6NTMxNzUwODA2OX0.fN9ffe_Vbw3rUrOWMSeo3nULXrUBT8VSJJiGUBiiRXE
http DELETE http://localhost:8087/api/v1/estacion/22 -A bearer -a eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpc3MiOiJzZXJzb21vbiIsImV4cCI6NTMxNzUwODA2OX0.fN9ffe_Vbw3rUrOWMSeo3nULXrUBT8VSJJiGUBiiRXE
http DELETE http://localhost:8087/api/v1/aparcamiento/14 -A bearer -a eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpc3MiOiJzZXJzb21vbiIsImV4cCI6NTMxNzUwODA2OX0.fN9ffe_Vbw3rUrOWMSeo3nULXrUBT8VSJJiGUBiiRXE
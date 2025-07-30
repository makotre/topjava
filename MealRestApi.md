GET
curl --location 'http://localhost:8080/topjava/rest/meals/100003' \
--header 'Authorization: Basic Og=='


DELETE
curl --location --request DELETE 'http://localhost:8080/topjava/rest/meals/100005' \
--header 'Authorization: Basic Og=='


getAll
GET
curl --location 'http://localhost:8080/topjava/rest/meals' \
--header 'Authorization: Basic Og=='


create
POST
curl --location 'http://localhost:8080/topjava/rest/meals' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic Og==' \
--data '{
"dateTime": "2025-07-30T13:00:00",
"description": "Тестовый обед",
"calories": 750
}'


update
PUT
curl --location --request PUT 'http://localhost:8080/topjava/rest/meals/100003' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic Og==' \
--data '{
"id": 100003,
"dateTime": "2025-07-29T14:00:00",
"description": "Завтрак обновлён",
"calories": 800
}'


getBetween
GET
curl --location 'http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&startTime=07%3A00&endDate=2020-01-30&endTime=11%3A00' \
--header 'Authorization: Basic Og=='

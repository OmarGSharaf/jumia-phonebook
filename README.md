# Jumia exercise

## Description

Create a single page application in Java (Frameworks allowed) that uses the provided database (SQLite 3) to list and categorize country phone numbers.

Phone numbers should be categorized by country, state (valid or not valid), country code and number.

The page should render a list of all phone numbers available in the DB. It should be possible to filter by country and state. Pagination is an extra.

## Docker
### Build
```
docker-compose build
```
### Run
```
docker-compose up
```
### frontend
```
localhost
```

## API documentation

Access the following endpoint through your favorite browser

`http://localhost:8080/swagger-ui`

## Tech

* [Docker] - makes it easier to create, deploy, and run applications by using containers.
* [Maven] - software project management and comprehension tool.
* [Spring boot] - java-based framework used to create a micro Service.
* [VueJs] - an open-source JavaScript framework for building user interfaces and single-page applications.

## Environment
* Docker version 20.10.7
* docker-compose version 1.28.6
* Ubuntu 20.04.2 LTS

[Docker]: <https://www.docker.com/>
[Maven]: <https://maven.apache.org/>
[Spring boot]: <https://spring.io/projects/spring-boot>
[VueJs]: <https://vuejs.org>
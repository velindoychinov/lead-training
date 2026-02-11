
# Lead Training

![Build Status](https://github.com/velindoychinov/lead-training/actions/workflows/maven-ci.yml/badge.svg)

This is a Spring Boot training project demonstrating a layered REST architecture
(controllers, services, repositories), DTO mapping, and integration testing with
MockMvc and H2 in-memory database.

The project contains:
- CRUD controllers (Course, StudyGroup, Student, etc.)
- Registration endpoints (student ↔ course, student ↔ group, teacher ↔ course, teacher ↔ group)
- Reporting endpoints
- Integration tests using real services and repositories

---

## Requirements

- Java 17+
- Maven 3.9+

---

## How to build

```bash
mvn clean install
```

## How to run

```bash
mvn spring-boot:run
```

Option to pass .env
```
# Unix / Mac
export $(grep -v '^#' .env | xargs)
./mvnw spring-boot:run
```

```
# Windows (PowerShell)
Get-Content .env | Foreach-Object { if ($_ -and $_ -notmatch '^#') { $parts = $_ -split '='; set-item env:$($parts[0]) $parts[1] } }
mvn spring-boot:run
```

## Swagger UI

After starting the application, Swagger UI is available at:

```bash
http://localhost:8080/swagger-ui/index.html
```

It provides a full interactive documentation and playground of all REST endpoints.

## H2 Console

The application uses an file/in-memory H2 database by default.

H2 console is available at:

```bash
http://localhost:8080/h2-console
```

Typical settings:

```
JDBC URL: jdbc:h2:mem:training_db
User: sa
Password: (empty)
```

## Tests

The project contains mainly integration tests using MockMvc.

To run tests:

```bash
mvn test
```

Tests verify:

controller endpoints

real service logic

real repositories

transactional behavior using an in-memory database

## Project structure

```
src/main/java
 └─ controller
 └─ service
 └─ repository
 └─ dto
 └─ entity
 └─ config

src/test/java
 └─ integration tests
 └─ test utilities (TestDataUtil, etc.)
```

## License

Apache License 2.0


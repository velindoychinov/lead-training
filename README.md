# Lead Training Project

![Build](https://github.com/velindoychinov/lead-training/actions/workflows/java-ci.yml/badge.svg?branch=main)
[![Coverage](https://codecov.io/gh/velindoychinov/lead-training/branch/main/graph/badge.svg)](https://codecov.io/gh/velindoychinov/lead-training)

This is a Spring Boot training project demonstrating REST APIs, layered architecture,
integration tests, and H2 in‑memory DB.

---

## 🚀 Features

- 🌐 **Swagger UI**: Interactive API documentation and playground
  http://localhost:8080/swagger-ui/index.html

- 🛠 **H2 Console** (in‑memory database / file):  
  http://localhost:8080/h2-console

- 🧪 **Integration Tests** using MockMvc and H2  
- 💡 Clean service, repository, controller separation

---

## 📌 Requirements

- Java 17+
- Maven 3.9+

---

## 🛠 Installation

Build the project:

```bash
mvn clean install
```

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


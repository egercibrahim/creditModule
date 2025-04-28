# Credit Module API

This is a backend application for a bank's loan management system.

Built with:
- Java 21
- Spring Boot 3.4
- Maven
- H2 In-memory Database
- Spring Security (Admin/Customer Roles)
- Swagger/OpenAPI
- JUnit5 + MockMvc (Testing)

---

## Features

- Create Users (ADMIN, CUSTOMER)
- Create Customers linked to Users
- Create Loans linked to Customers with Installment Options (6, 9, 12, 24 months)
- List Loans (with filters: installments, isPaid)
- List Installments for Loans
- Pay Installments (early payment discount / late payment penalty)
- Role-based Access Control:
    - ADMIN: manage all users/customers/loans/installments
    - CUSTOMER: manage their own loans/installments

---

## How to Build and Run

### 1. Clone Project
```bash
git clone https://github.com/egercibrahim/creditModule.git
cd creditModule
```

### 2. Build
```bash
mvn clean install
```

### 3. Run
```bash
mvn spring-boot:run
```

Application will be available at:
```
http://localhost:8080
```

---

## Authentication

All endpoints require Basic Authentication.

| Username  | Password  | Role      |
|:--------- |:----------|:--------- |
| admin     | admin     | ADMIN     |
| customer  | customer  | CUSTOMER  |

(You can create additional users through the API.)

---

## API Documentation

Swagger UI available at:
```
http://localhost:8080/swagger-ui/index.html
```

Test and explore all APIs easily!

---

## Example Endpoints

### ✦ User
- **POST** `/api/v1/credit/user` : Create a User

### ✦ Customer
- **POST** `/api/v1/credit/customer` : Create a Customer

### ✦ Loan
- **POST** `/api/v1/credit/loan` : Create a Loan
- **GET** `/api/v1/credit/loan` : List Loans

### ✦ Installments
- **GET** `/api/v1/credit/installment?loanId=1` : List Installments
- **POST** `/api/v1/credit/installment` : Pay Installments

---

## Configuration

Important settings are in `application.properties`:
```properties
spring.datasource.url=jdbc:h2:mem:credit
spring.h2.console.enabled=true
spring.jpa.show-sql=false
spring.jpa.hibernate.ddl-auto=update
server.error.include-message=always
```

H2 Console available at:
```
http://localhost:8080/h2-console
```
(JDBC URL: `jdbc:h2:mem:testdb`)

---

## Tests

- Full Unit Tests for Controllers and Services
- Authentication/Authorization tests
- Payment (reward/penalty) logic tested

Run tests with:
```bash
mvn test
```

---

## Technologies Used

| Tech                | Purpose                         |
|:--------------------|:--------------------------------|
| Java 21             | Core Programming Language       |
| Spring Boot 3.4     | Backend Framework               |
| Spring Security     | Authentication/Authorization    |
| H2 Database         | In-memory Database               |
| Maven               | Dependency Management           |
| Swagger             | API Documentation               |
| JUnit5 + MockMvc    | Testing                         |

---

## Project Status

> Production-ready! ✨
> Secure, tested, documented and cleanly structured.

---

# 🚀 Ready to deploy!


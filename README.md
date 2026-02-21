# TalkTo Blog

## Overview

TalkTo is a Spring Boot–based web application that implements a secure blogging platform.

The project is designed not only as a functional web application, but also as a structured demonstration of backend architecture, security practices, database management, and multi-layer testing strategy.

It serves as a realistic foundation for exploring production-grade patterns in authentication, persistence, validation, and automated verification.

---

## Technologies Used

- Java 21
- Spring Boot 3.5
- Spring MVC
- Spring Security
- Spring Data JPA
- Thymeleaf
- Hibernate
- Flyway (database migrations)
- PostgreSQL
- Java Mail (SMTP)
- Bootstrap 5 (UI)
- MailHog (local email testing)
- Testcontainers
- Mockito
- JUnit 5
- Selenium
- Cucumber

---

## Architecture Overview

The application follows a classical layered architecture with clear separation of concerns:

- **Controller Layer** – Handles HTTP requests and response mapping
- **Service Layer** – Encapsulates business logic and transaction boundaries
- **Repository Layer** – Data access abstraction via Spring Data JPA
- **Domain Layer** – JPA entities representing persistence model
- **Configuration Layer** – Security, mail, and infrastructure configuration

Design decisions:

- Open Session in View is disabled to prevent hidden lazy loading
- Hibernate `ddl-auto` is set to `validate`
- Database schema is managed exclusively via Flyway migrations
- Email delivery is abstracted behind a `MailService` interface

---

## Implemented Features

### User Management

- User registration with validation
- Password encryption using BCrypt
- Secure login via Spring Security
- Logout
- Profile view and edit
- Unique email constraint

### Password Reset

- Request password reset via email
- Random UUID token generation
- Token stored hashed (SHA-256)
- Token expiration (30 minutes)
- Token marked as used after reset
- Previous tokens invalidated
- Protection against account enumeration
- Mail delivery via SMTP (MailHog locally)

### Posts

- Authenticated users can create posts
- Posts are linked to authors
- Posts are listed with author and timestamp
- Posts are viewable publicly

### Comments

- Authenticated users can add comments
- Comments are linked to posts and authors
- Comments displayed with author and timestamp

---

## Security

### Public Routes

- `/`
- `/register`
- `/login`
- `/password/**`

### Authenticated Routes

- `/posts/**`
- `/profile/**`

### Restrictions

- `/admin/**` is explicitly denied
- Custom 403 and 404 handling
- CSRF enabled (Spring Security default)

---

## Error Handling

- `GlobalExceptionHandler`
- Custom 404 page
- Custom 403 page
- Generic error page

---

## Database Structure

Managed via Flyway migrations.

### Tables

- `users`
- `posts`
- `comments`
- `password_reset_tokens`

### Key Constraints

- Foreign keys with `ON DELETE CASCADE`

### Indexes

- `posts.author_id`
- `comments.post_id`
- `comments.author_id`
- `password_reset_tokens.token_hash`
- `password_reset_tokens.user_id`

---

## Setup Instructions

### Prerequisites

- Java 21
- Maven
- PostgreSQL
- MailHog (for local email testing)

### Configure Environment Variables

```
TALKTO_DB_URL=jdbc:postgresql://localhost:5432/talkto
TALKTO_DB_USERNAME=your_username
TALKTO_DB_PASSWORD=your_password
```

### Run Database Migrations

Flyway runs automatically on application startup.

### Run MailHog

Download MailHog and run:

```
./MailHog
```

Defaults:

- SMTP: `localhost:1025`
- Web UI: `http://localhost:8025`

### Start Application

```
http://localhost:8080
```

---

## Project Structure

```
src/main/java/com/blog
│
├── config
│
├── common
│   ├── exception
│   └── util
│
├── user
│   ├── controller
│   ├── service
│   ├── repository
│   ├── domain
│   └── dto
│
├── post
│   ├── controller
│   ├── service
│   ├── repository
│   ├── domain
│   └── dto
│
├── comment
│   ├── controller
│   ├── service
│   ├── repository
│   ├── domain
│   └── dto
│
├── auth
│   ├── controller
│   ├── service
│   └── security
│
└── mail
    └── service
```

---

## Testing Strategy

The project implements a multi-layer testing strategy aligned with real-world backend systems.

### Unit Tests

- Isolated tests for business logic
- No Spring context
- Mockito-based
- Focus on deterministic service-layer behavior

### Integration Tests

- Full Spring Boot context
- Real PostgreSQL instance via Testcontainers
- Flyway migrations executed automatically
- Validate repository behavior and persistence consistency

### Controller Tests

- Spring Test (MockMvc)
- Validate HTTP contract without full browser execution

### End-to-End (Technical)

- Selenium + JUnit
- Validates complete browser-level flows
- Focus: system wiring and UI integration

### End-to-End (Behavior-Driven / BDD)

- Cucumber + Gherkin scenarios
- Business-readable behavioral specifications
- Demonstrates Behavior-Driven Development approach

> In production systems, typically a single E2E approach is selected to avoid duplication.
> Both approaches are included intentionally to demonstrate understanding of different testing strategies and their trade-offs.

---

## Purpose of the Project

This project serves as:

- A structured backend reference implementation
- A demonstration of secure authentication and password reset flow
- A practical example of layered architecture with database migrations
- A platform for exploring different automated testing approaches

---

## Production Considerations

Although this project is primarily educational, it incorporates production-oriented decisions:

- Database schema controlled via migrations (Flyway)
- Strict validation mode for Hibernate
- Disabled Open Session in View
- Token hashing before persistence
- Clear separation of layers and responsibilities
- Real database testing via Testcontainers
- Explicit route security configuration

The architecture is intentionally designed to allow extension with:
- Containerization (Docker)
- Centralized logging
- Externalized configuration
- CI/CD integration

## Author

**Oleksii Bilohryshchenko**
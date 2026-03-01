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
- Postman
- Newman (CLI API test runner)
- Node.js (for API tests and Playwright performance testing)
- k6 (load testing)

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

### Running API Tests

Prerequisites:
- Node.js 20+
- Running Spring Boot application
- Configured database

Run:

npm install
npm run api:test

---

A stress_seed.sql script is included to pre-populate the database
before stress testing to ensure consistent performance behavior
independent of table size.

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

## Performance & Load Testing

The application was tested under controlled load using k6 and browser-level lifecycle measurements via Playwright.

### Load Profile

- 10 RPS (constant arrival rate)
- 30s ramp-up
- 5 minutes steady load
- 30s ramp-down

Scenario distribution:

- Read-only: 3 RPS
- Read + Comment: 3 RPS
- Create Post: 2 RPS
- Update Profile: 2 RPS

Total: 10 RPS

### Backend Results (k6)

- Total HTTP requests: 15,786
- Failed requests: 0 (0.00%)
- Average latency: 23.11 ms
- p95 latency: 106.61 ms
- p99 latency: 107.58 ms

All defined thresholds were satisfied:
- p(95) < 1500 ms
- p(99) < 2000 ms
- http_req_failed < 1%

### Browser Lifecycle (Playwright)

Measured:
- DNS
- TCP
- TLS
- TTFB
- DOMContentLoaded
- Load Event

The combined approach ensures structured backend SLA validation and browser lifecycle visibility.

Detailed results available in:
docs/load-testing-report.md

### API Contract Testing

- Postman collection with full endpoint coverage
- Newman CLI execution
- HTML reporting
- CI-enforced API verification

## API Testing (Postman + Newman)

The project includes a fully automated API test suite built with Postman and executed via Newman CLI.

### Coverage

The test suite validates:

- Public endpoints (registration, password reset, pages)
- Protected endpoints (posts, profile, comments)
- Security policy enforcement (403, redirects, CSRF validation)
- Dynamic resource creation and ID extraction
- Full E2E API flow (register → login → create post → comment → verify)

The collection contains:

- Helper scripts for session handling
- Dynamic variable extraction
- CSRF stabilization logic
- No hardcoded resource IDs

Total coverage:
- 100+ requests
- 90+ assertions
- 0 test dependency coupling

## Stress Testing

The application was stress tested separately for:

- Authorization throughput
- Read operations
- Post creation
- Comment creation

Testing was executed using k6 in Docker-isolated environment.

Under extreme load (up to 1000 concurrent users):

- Read and comment operations remained stable.
- Post creation showed increased latency due to BCrypt password verification.
- No request failures occurred during stress tests.

Detailed results are documented in the laboratory report.

### Local Execution

Run API tests locally:
npm install
npm run api:test

An HTML report will be generated: reports/api-report.html


### CI Integration

API tests are executed automatically in GitHub Actions.

Pipeline steps:

1. Build Spring Boot application
2. Start application
3. Execute Newman test suite
4. Generate HTML report
5. Upload report as CI artifact

CI guarantees:

- Deterministic database state (Flyway)
- Stable session authentication
- CSRF validation consistency

CI pipeline fails on any API regression.

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
- Automated API verification via CI pipeline
- HTML test reports for inspection 
- Explicit load validation under 10 RPS profile
- Measured backend latency under defined normal load (10 RPS)
- Observed p95 latency ≈ 106 ms
- Latency well below defined threshold limits
- Separate browser lifecycle measurement to validate UX readiness

Load testing was executed using isolated Docker containers
to reduce measurement distortion between the application
and the load testing tool.

## Author

**Oleksii Bilohryshchenko**
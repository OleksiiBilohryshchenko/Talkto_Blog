# TalkTo Blog

Overview
TalkTo is a Spring Boot–based web application that implements a simple blogging platform.

The primary goal of this project is to provide a working web application that will be used for further automated testing.

The application allows users to register, create posts, leave comments, manage their profile, and reset their password securely.

Technologies Used:

``Java 21/
Spring Boot 3.5/
Spring MVC/
Spring Security/
Spring Data JPA/
Thymeleaf/
Hibernate/
Flyway (database migrations)/
PostgreSQL/
Java Mail (SMTP)/
Bootstrap 5 (UI)/
MailHog (local email testing)``

Architecture Overview

* The application follows a layered architecture:

* Controller Layer – handles HTTP requests

* Service Layer – business logic

* Repository Layer – data access via Spring Data JPA

* Domain Layer – JPA entities

* Configuration Layer – security, mail, beans

Database schema is managed via Flyway migrations.

Implemented Features

* User Management
   User registration with validation
   
* Password encryption using BCrypt
   
* Secure login via Spring Security
   
* Logout
   
* Profile view and edit
   
* Unique email constraint

Password Reset (Secure Implementation)
*
   Request password reset via email
* 
   Random UUID token generation
* 
   Token stored hashed (SHA-256)
* 
   Token expiration (30 minutes)
* 
   Token marked as used after reset
* 
   Previous tokens invalidated
* 
   Protection against account enumeration
* 
   Mail delivery via SMTP (MailHog locally)

Posts

*    Authenticated users can create posts
*    Posts are linked to authors
*    Posts are listed with author and timestamp
*    Posts are viewable publicly

Comments
*    Authenticated users can add comments
*    Comments are linked to posts and authors
*    Comments displayed with author and timestamp

Security

   Public routes:
*    /
*    /register
*    /login
*    /password/**
*    Authenticated routes:
*    /posts/**
*    /profile/**
*    /admin/** explicitly denied
*    Custom 403 and 404 handling
*    CSRF enabled (default Spring Security)

Error Handling
*    GlobalExceptionHandler
*    Custom 404 page
*    Custom 403 page
*    Generic error page

Database Structure

Managed via Flyway migrations.

Tables

* users
* posts
* comments
* password_reset_tokens

Key Constraints

Foreign keys with ON DELETE CASCADE

Indexes for performance:
* posts.author_id
* comments.post_id
* comments.author_id
* password_reset_tokens.token_hash
* password_reset_tokens.user_id

Setup Instructions

Prerequisites

* Java 21
* Maven
* PostgreSQL
* MailHog (for local email testing)

Configure Environment Variables:
* TALKTO_DB_URL=jdbc:postgresql://localhost:5432/talkto
* TALKTO_DB_USERNAME=your_username
* TALKTO_DB_PASSWORD=your_password

Run Database Migrations

Flyway runs automatically on application startup.

Run MailHog (Optional but Recommended)

Download MailHog and run:
``./MailHog``

Default:

* SMTP: localhost:1025
* Web UI: http://localhost:8025

Start Application:

``http://localhost:8080``

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

Notes

* Hibernate ddl-auto is set to validate
* Open-in-view is disabled
* Tokens are hashed before storage
* Passwords are encrypted using BCrypt
* Email sending is abstracted via MailService interface

_**Purpose of the Project**_

This project serves as:

* A baseline web application
* A target system for automated testing exercises
* A demonstration of secure authentication and password reset implementation

Author

**Oleksii Bilohryshchenko**
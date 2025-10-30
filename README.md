📖 Book Social API
This repository contains the backend RESTful API for the Book Social platform, a service designed for users to discover, rate, and review books and authors.

The application is built with Java 17 and the Spring Boot 3 framework, emphasizing a secure, robust, and enterprise-ready architecture. It features a complete JWT-based authentication system, role-based access control, and a production-ready setup using Docker and Flyway for database migrations.

✨ Core Features
Secure Authentication: Full JWT-based authentication flow using Access Tokens (2 hours) and Refresh Tokens (4 hours).

Role-Based Authorization (RBAC): Method-level security using @PreAuthorize to protect admin-only endpoints (hasAuthority('ADMIN')) versus user-accessible ones.

Database Version Control: Uses Flyway to manage and version database schema changes, ensuring consistency across all environments.

DTO Pattern: Strictly separates JPA Entities from the API by using Data Transfer Objects (DTOs) for requests (DtoBookIU) and responses (DtoBook).

Pagination: Implements efficient data retrieval for lists using a custom PageableRequest and PagerUtil.

Centralized Error Handling: Uses @ControllerAdvice and a GlobalExceptionHandler to provide consistent, well-formatted JSON error responses for all exceptions.

Validation: Leverages jakarta.validation constraints on DTOs to ensure data integrity before it reaches the service layer.

Unit Testing: Service layer logic is validated with JUnit 5 and Mockito, as demonstrated in AuthorServiceImplTest.

Containerized: Fully containerized with a multi-stage Dockerfile and a docker-compose.yml for easy setup.

🛠️ Technology Stack
Framework: Spring Boot 3.5.6

Language: Java 17

Security: Spring Security 6, JSON Web Token (jwt)

Database: PostgreSQL

Data Access: Spring Data JPA (Hibernate)

DB Migration: Flyway

Build Tool: Apache Maven

Containerization: Docker & Docker Compose

Testing: JUnit 5, Mockito

🚀 Getting Started
Prerequisites
Java 17 (or newer)

Apache Maven 3.8+

Docker & Docker Compose

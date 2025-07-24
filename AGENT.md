# Library Application - Agent Guide

## Build/Test/Lint Commands

- **Build**: `./mvnw clean compile` or `./mvnw`
- **Run application**: `./mvnw spring-boot:run`
- **Run tests**: `./mvnw test` (unit tests) or `./mvnw verify` (all tests including integration)
- **Run single test**: `./mvnw -Dtest=ClassName#methodName test`
- **Run integration tests**: `./mvnw integration-test` or tests ending in `*IT`
- **Lint/Format Java**: `./mvnw spotless:apply`
- **Check style**: `./mvnw checkstyle:check`
- **Frontend build**: `./npmw build`
- **Frontend lint**: `./npmw run lint`

## Architecture & Structure

- **Framework**: Spring Boot 3.4.5 + JHipster 8.11.0
- **Database**: MySQL with Liquibase migrations
- **Main class**: `com.giodad.todolist.LibraryApp`
- **Entities**: Libro, Autore, Recensione with JPA relationships
- **Layers**: Domain → Service → DTO → REST Controllers
- **Frontend**: React + TypeScript in `src/main/webapp`
- **Security**: OAuth2 resource server + JWT

## Code Style & Conventions

- **Package**: `com.giodad.todolist.*`
- **Mappers**: MapStruct with `@Mapper(componentModel = "spring")`
- **DTOs**: Separate DTOs for each entity in `service.dto` package
- **Tests**: Unit tests (`*Test.java`) vs Integration tests (`*IT.java`)
- **Naming**: CamelCase for Java, kebab-case for frontend
- **Imports**: Group by standard Java conventions

# Spring MVC Security Course Repository

This repository contains a Spring Boot MVC application used to study authentication, authorization, and account lifecycle flows with Spring Security.

The runnable application lives in [`3865-seguranca-java/`](./3865-seguranca-java).

## Project overview

The application models a small medical scheduling system with three user profiles:

- **ATENDENTE**: manages patients, doctors, and consultations.
- **MEDICO**: can view consultations related to their account.
- **PACIENTE**: can create an account, activate it by email, log in, and manage their own consultations.

Main capabilities implemented in the project include:

- form-based login with Spring Security;
- role-based authorization for doctors, patients, and attendants;
- account creation and activation by email;
- password change for authenticated users;
- password recovery using temporary tokens;
- CRUD flows for doctors and patients;
- consultation scheduling and listing;
- Flyway database migrations for PostgreSQL.

## Tech stack

- Java 21
- Spring Boot 3.3
- Spring MVC
- Spring Security
- Spring Data JPA
- Thymeleaf + Thymeleaf Layout Dialect
- Flyway
- PostgreSQL
- Spring Mail
- Maven Wrapper

## Repository structure

```text
.
├── README.md
├── securitycontext.txt
└── 3865-seguranca-java/
    ├── pom.xml
    ├── mvnw
    ├── src/main/java/
    └── src/main/resources/
```

## Application modules

### MVC controllers

- `IndexController`: serves the home page.
- `LoginController`: login, logout, and password change flows.
- `UserController`: patient self-service account creation and account activation.
- `RecuperacaoContaController`: forgot-password and account recovery flows.
- `MedicoController`: doctor management and specialty lookup.
- `PacienteController`: patient management.
- `ConsultaController`: consultation scheduling and listing.

### Security rules

The security configuration allows public access to static assets, the home page, login-adjacent pages, account creation, and account activation. Other endpoints require authentication, with route restrictions based on role.

Examples:

- `/pacientes/**` requires `ROLE_ATENDENTE`.
- `GET /medicos` allows `ROLE_ATENDENTE` and `ROLE_PACIENTE`.
- `/medicos/**` otherwise requires `ROLE_ATENDENTE`.
- `/consultas` allows `ROLE_ATENDENTE`, `ROLE_PACIENTE`, and `ROLE_MEDICO`.
- `POST /consultas/formulario` allows `ROLE_ATENDENTE` and `ROLE_PACIENTE`.

The application also configures:

- custom login page at `/login`;
- logout invalidating the HTTP session and deleting `JSESSIONID`;
- remember-me support;
- secure session cookie settings.

## Prerequisites

Before running the project, make sure you have:

- Java 21
- a PostgreSQL database
- access to an SMTP account for email delivery

## Required environment variables

The application reads its database and email configuration from environment variables referenced in `application.properties`.

Set the following variables before starting the app:

```bash
export DB_HOST=localhost:5432
export DB_NAME=vollmed
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
export EMAIL_USERNAME=your-email@example.com
export EMAIL_PASSWORD=your-email-password-or-app-password
```

> **Note:** for Gmail SMTP you will usually need an app password instead of your normal account password.

## Running locally

From the repository root:

```bash
cd 3865-seguranca-java
./mvnw spring-boot:run
```

Or package first and then run:

```bash
cd 3865-seguranca-java
./mvnw clean package
java -jar target/web-application-0.0.1-SNAPSHOT.jar
```

By default, Spring Boot serves the app locally at:

- `http://localhost:8080`

## Database notes

- The project uses **Flyway** migrations under `src/main/resources/db/migration`.
- JPA is configured with `spring.jpa.hibernate.ddl-auto=update`.
- PostgreSQL is the configured database dialect.

A typical database bootstrap flow is:

1. create an empty PostgreSQL database;
2. export the environment variables;
3. start the application;
4. let Flyway apply the migrations automatically.

## Authentication and account flows

### 1. Sign in

Users authenticate through the custom login page at `/login`.

### 2. Patient self-registration

Patients can create an account through `/criar-conta`. After registration, the application sends an activation email and the account must be activated before login.

### 3. Account activation

The activation link points to `/ativar-conta?token=...`.

### 4. Password recovery

Users can request a recovery token at `/esqueci-minha-senha` and complete the reset at `/recuperar-conta`.

### 5. Password change

Authenticated users can change their password at `/alterar-senha`. After a successful change, the session is invalidated and the user must log in again.

## Domain summary

- **Usuários** store credentials, role, activation state, and recovery/activation token data.
- **Médicos** include CRM and specialty.
- **Pacientes** include CPF and contact data.
- **Consultas** relate a doctor, a patient, a date, and a specialty.

Consultation visibility is profile-aware:

- attendants see all consultations;
- doctors and patients see only their own consultations.

## Useful commands

### Run tests

```bash
cd 3865-seguranca-java
./mvnw test
```

### Create a production-style package

```bash
cd 3865-seguranca-java
./mvnw clean package
```

## Suggested improvements

If you want to keep evolving the project, some natural next steps are:

- add automated integration and security tests;
- provide a `docker-compose.yml` for PostgreSQL and MailHog;
- document seed data or add development fixtures;
- separate activation tokens and password reset tokens;
- externalize configuration with Spring profiles.

## License

This repository does not currently include a license file. Add one if you plan to distribute or reuse the code publicly.

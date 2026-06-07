# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

```bash
# Build the project (includes unit tests only)
./mvnw package

# Run only unit tests (skips *IT.java integration tests)
./mvnw test

# Run a single unit test class
./mvnw test -Dtest=TransactionServiceTest

# Run a single test method
./mvnw test -Dtest=TransactionServiceTest#testExists

# Run integration tests only
./mvnw failsafe:integration-test verify -Pintegration-tests

# Run all tests (unit + integration)
./mvnw test -Pall-tests

# Start PostgreSQL via Docker Compose (required for the app and integration tests)
docker compose up -d
```

## Architecture Overview

This is a **Spring Boot 4.0** REST API built with **Java 21**, Maven, PostgreSQL, and H2 (for tests).

The codebase follows a **Domain-Driven layered architecture** with a custom Repository pattern — domain interfaces are defined in the domain layer while JPA implementations live in the infrastructure layer, keeping the domain free of persistence concerns.

### Layer Map (top to bottom)

```
interfaces/          External adapters: Google Sheets API client, OAuth request initializers
application/         Controllers, Business Managers (orchestration), DTOs, MapStruct mappers
domain/
  core/              Domain models, service interfaces+impls, repository interfaces
  file/              CSV file parsing pipeline (readers → converters → evaluator)
  sheets/            Google Sheets range-fetching service
infra/               JPA entities, Spring Data JPA repositories, repository impls
```

### Request Flow

```
Controller  →  BusinessManager  →  Service (interface)  →  Repository (interface)
                                        ↑                        ↑
                                   ServiceImpl              RepositoryImpl
                                                             ↓
                                                      JpaRepository (Spring Data JPA)
```

- **Controllers** (`application/controller`): Thin REST layer, delegates to Business Managers.
- **Business Managers** (`application/manager`): Orchestrate domain services and MapStruct mappers. They validate cross-entity constraints (e.g., that referenced Category/Account exist before creating a Transaction).
- **Services** (`domain/core/service`): Define the `Service<T>` interface (exists, getAll, getById, create, createAll, update, deleteById). Implementations delegate to repository interfaces.
- **Repositories**: Domain interfaces (`domain/core/repository`) define the contract. Infrastructure implementations (`infra/repository/impl`) adapt Spring Data JPA repositories, mapping between `Entity` and domain `Model` objects.
- **Mappers**: MapStruct interfaces extending `GenericMapper<M, D>` in `application/mapper`. Configured with `componentModel = "spring"`.

### Key Domain Models

- **Transaction** — core entity. Has `date`, `amount`, `Category` reference, `Account` reference, `description`, plus ~10 additional optional fields (weekNumber, holiday, month, ticker, nbrOfActions, changeRate, isCommon, beforeConversion, currency, year, reference, tags). Tags is a `List<String>` backed by `@ElementCollection`.
- **Account** — id, name, creationDate.
- **Category** — id, name, creationDate.

### Entity ↔ Model Mapping Convention

JPA entities (in `infra/entity`) manage their own mapping to/from domain models:
- `Entity.from(Model)` — static factory, returns Entity.
- `entity.fromThis()` — instance method, returns domain Model.

Repositories call these in their impls so the domain layer never sees JPA types.

### CSV File Processing Pipeline

`domain/file/` implements a pipeline for evaluating CSV data against expected field configurations:

1. **Reader** (`CsvReader`): Splits header line + data lines.
2. **Converter** (`ConverterFactory` → `StringArrayConverter` or `StringListConverter`): Converts raw strings into arrays/lists.
3. **Evaluator** (`EvaluatorService`): Creates `RecordEvaluated` objects with validation results per field.

Field definitions are configurable via `record-fields.properties` (with `_fr_FR` locale variant).

### Google Sheets Integration

`interfaces/client/SheetsClient` wraps the Google Sheets API. Authentication is configurable via `application.properties` keys:
- `google.auth.request.initializer=service-account` (or `oauth`)
- Credential paths point to JSON key files outside the repo.

### Database Migrations

Liquibase changelogs in `src/main/resources/db/changelog/`. The master file (`db.changelog-master.xml`) includes individual change files. H2 (in-memory) is used for unit/integration tests; PostgreSQL for production.

## Test Conventions

- **Unit tests**: `*Test.java` — use `@SpringBootTest` with `@MockitoBean` on the repository dependency. The service under test is injected via `@Autowired`. Assertions use AssertJ.
- **Integration tests**: `*IT.java` — extend `CashflowBaseIntegrationTest` which provides `MockMvc` and the `/api/v1` base path constant. Use `@Sql` to seed data. H2 is the test database with `ddl-auto=create-drop`.
- **Architecture tests**: `CashflowArchTest` / `CashflowLayeredArchTest` use ArchUnit to enforce layering rules.
- Jacoco enforces a minimum 60% complexity coverage ratio via the `jacoco-maven-plugin`.
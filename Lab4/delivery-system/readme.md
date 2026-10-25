# Delivery System

A Spring Boot application using Spring Data JPA and PostgreSQL to manage delivery system data.

## Files

- `pom.xml` – Defines project dependencies and Maven configuration.
- `application.properties` – Configures the PostgreSQL connection and JPA settings.
- `schema.sql` – Creates the database tables, primary keys, foreign keys, and integrity constraints.
- `DeliverySystemApplication.java` – Starts the application and tests the customer repository using `CommandLineRunner`.
- `Customer.java` – Defines the Customer entity mapped to the `customers` table.
- `CustomerRepository.java` – Provides database operations for customers using Spring Data JPA.

# Spring Boot Multi-Environment File Reader

This project demonstrates a Spring Boot application that reads data from different files depending on the active environment (`dev` or `prod`). It uses configuration properties, conditional bean creation with SpEL, and Spring Boot Actuator.

## Project Structure

### Java Files

- **`DemoApplication.java`** — Main class that starts the Spring Boot application.
- **`ConfigDemo.java`** — Demonstrates configuration property injection and configuration precedence.
- **`DataFileProperties.java`** — Loads file-related settings, such as the file path and format, using `@ConfigurationProperties`.
- **`PersonRecord.java`** — Represents a record read from a data file.
- **`DataFileReader.java`** — Interface defining the common methods for reading data files.
- **`CsvDataFileReader.java`** — Reads records from a CSV file. Activated for the development profile when the configured format is `csv`.
- **`JsonDataFileReader.java`** — Reads records from a JSON file. Activated for the production profile when the configured format is `json`.
- **`DataFileStartupRunner.java`** — Loads the configured file at application startup and prints information about the loaded records.
- **`DataSourceInfoContributor.java`** — Adds information about the active data source, file format, and number of loaded records to the Actuator `/actuator/info` endpoint.

### Configuration Files

- **`application.properties`** — Defines the application's default configuration.
- **`application-dev.yml`** — Contains development-specific settings, including the CSV file path and format.
- **`application-prod.yml`** — Contains production-specific settings, including the JSON file path and format.

### Data Files

- **`dev.csv`** — Contains sample data for the development environment.
- **`prod.json`** — Contains sample data for the production environment.

## Running the Application

Activate the development profile:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

Activate the production profile:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

The application automatically selects the appropriate configuration and reader based on the active profile and configured file format.

## Actuator

If Actuator web exposure is configured, data-source information is available at:

`http://localhost:8080/actuator/info`

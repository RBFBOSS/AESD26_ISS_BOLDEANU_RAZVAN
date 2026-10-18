
# Spring Boot Dependency Injection Demo

This project demonstrates the major types of dependency injection in Spring Boot using a simple car-starting scenario. It illustrates constructor, field, setter, and method injection, including the order in which they are performed.

## Project Structure

### Java Files

- **`Lab3Application.java`** — Main class that starts the Spring Boot application and runs the car-starting scenario using `CommandLineRunner`.
- **`CarStarter.java`** — Main component that receives dependencies through constructor, field, setter, and method injection. It coordinates the components required to start the car.
- **`Battery.java`** — Represents the car battery and supplies electrical power.
- **`FuelSystem.java`** — Represents the fuel system and supplies fuel to the engine.
- **`IgnitionSystem.java`** — Represents the ignition system and ignites the fuel-air mixture.
- **`StarterMotor.java`** — Represents the starter motor and turns the engine over.

## Dependency Injection Types

- **Constructor injection** — `Battery` is injected through the `CarStarter` constructor.
- **Field injection** — `FuelSystem` is injected directly into a field using `@Autowired`.
- **Setter injection** — `IgnitionSystem` is injected through the `setIgnitionSystem()` method.
- **Method injection** — `StarterMotor` is injected through the `configureStarter()` method.

## Injection Order

The application prints messages to demonstrate the injection sequence:

1. **Constructor injection** — The `Battery` dependency is received.
2. **Field injection** — The `FuelSystem` dependency is injected.
3. **Setter injection** — The `IgnitionSystem` dependency is received, and the application verifies that field injection has already occurred.
4. **Method injection** — The `StarterMotor` dependency is received, and the application verifies that field injection has already occurred.

The output demonstrates that constructor injection happens before field injection, followed by setter and method injection.

## Running the Application

Run `Lab3Application.java` from IntelliJ IDEA or execute the following command from the project directory:

```bash
mvn spring-boot:run
```

The application starts Spring Boot, creates the required beans, prints the dependency injection messages, and executes the car-starting scenario.


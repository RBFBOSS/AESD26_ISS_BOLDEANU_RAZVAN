# Delivery System

A Spring Boot application using Spring Data JPA and PostgreSQL to manage depots, vehicles, drivers, customers, and deliveries.

## Files

### Main Application
- `DeliverySystemApplication.java` – Starts the Spring Boot application and tests the customer repository using `CommandLineRunner`.

### Entities
- `Depot.java` – Defines the Depot entity, including its relationship with vehicles.
- `Vehicle.java` – Defines the Vehicle entity, including its depot and optional driver.
- `Driver.java` – Defines the Driver entity and its one-to-one relationship with a vehicle.
- `Customer.java` – Defines the Customer entity and its relationship with deliveries.
- `Delivery.java` – Defines the Delivery entity, including its associated customer, quantity, notes, and timestamp.

### Repositories
- `DepotRepository.java` – Provides database operations for depots.
- `VehicleRepository.java` – Provides database operations for vehicles, including a derived query, an explicit JPQL query, and a transactional modifying query.
- `DriverRepository.java` – Provides database operations for drivers.
- `CustomerRepository.java` – Provides database operations for customers using Spring Data JPA.
- `DeliveryRepository.java` – Provides database operations for deliveries.

### Services
- `DepotService.java` – Handles depot-related operations.
- `VehicleService.java` – Handles vehicle-related operations, including availability updates and depot-based searches.
- `DriverService.java` – Handles driver-related operations.
- `CustomerService.java` – Handles customer-related operations.
- `DeliveryService.java` – Handles delivery-related operations.

### Controllers
- `CustomerController.java` – Exposes REST CRUD endpoints for creating, retrieving, updating, and deleting customers.

## Customer REST Endpoints

Base URL: `http://localhost:8080/api/customers`

| HTTP Method | Endpoint | Operation |
|---|---|---|
| GET | `/api/customers` | Retrieve all customers |
| GET | `/api/customers/{id}` | Retrieve a customer by ID |
| POST | `/api/customers` | Create a customer |
| PUT | `/api/customers/{id}` | Update a customer |
| DELETE | `/api/customers/{id}` | Delete a customer |
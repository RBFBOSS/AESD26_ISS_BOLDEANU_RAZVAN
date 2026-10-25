# Library Management System

A Spring Boot application for managing book borrowing and returning. It includes configurable borrowing policies, dependency injection, AOP logging and validation, custom exceptions, and application events.

## Project Structure

### Main Application and Configuration
- `LibraryManagementApplication.java` – Starts the Spring Boot application.
- `application.yml` – Configures the application name, borrowing policy, and event threshold.

### Model
- `Member.java` – Represents a library member and their borrowed books.
- `Book.java` – Represents a book and its availability.
- `MemberRole.java` – Defines member roles.

### Repository
- `MemberRepository.java` – Retrieves and manages member data.
- `BookRepository.java` – Retrieves and manages book data.

### Policy
- `BorrowingPolicy.java` – Defines the borrowing policy interface.
- `FixedBorrowingPolicy.java` – Applies a fixed borrowing limit of three books.
- `RoleBasedBorrowingPolicy.java` – Applies borrowing limits based on member roles: student (2), regular (3), and premium (5).
- `BorrowingPolicyConfiguration.java` – Selects the active borrowing policy.

### Service and Controller
- `BorrowingService.java` – Implements borrowing and returning business logic.
- `BorrowingController.java` – Exposes REST endpoints for borrowing and returning books.

### Aspects
- `BorrowingValidationAspect.java` – Validates borrowing operations.
- `OperationLoggingAspect.java` – Logs operations and execution times.
- `ExceptionLoggingAspect.java` – Logs intercepted exceptions.

### Events
- `ManyBooksBorrowedEvent.java` – Represents an event triggered when a borrowing threshold is exceeded.
- `ManyBooksBorrowedListener.java` – Handles the published event.

### Exception
- `BorrowingException.java` – Represents borrowing-related errors.

## Running the Application

Run `LibraryManagementApplication.java` from IntelliJ IDEA. The application listens on `http://localhost:8080` when configured to use port 8080.

## Testing the API

Run the following commands in Windows CMD while the application is running.

### 1. Borrow Books

Borrow book 101 for member 1:

```cmd
curl.exe -i -X POST http://localhost:8080/api/borrowings/1/books/101
```

Borrow book 102 for member 1:

```cmd
curl.exe -i -X POST http://localhost:8080/api/borrowings/1/books/102
```

Try borrowing book 103 to test the borrowing limit:

```cmd
curl.exe -i -X POST http://localhost:8080/api/borrowings/1/books/103
```

### 2. Test Error Handling

Try borrowing a nonexistent book:

```cmd
curl.exe -i -X POST http://localhost:8080/api/borrowings/1/books/999
```

Try borrowing for a nonexistent member:

```cmd
curl.exe -i -X POST http://localhost:8080/api/borrowings/999/books/101
```

These requests should return error responses if the specified book or member does not exist.

### 3. Return a Book

If the controller uses `DELETE` for returning books:

```cmd
curl.exe -i -X DELETE http://localhost:8080/api/borrowings/1/books/101
```

The operation should succeed if member 1 has book 101 borrowed.

### 4. Test the Role-Based Policy

Set the following configuration in `application.yml` and restart the application:

```yaml
library:
  borrowing-policy: roleBased
  event-threshold: 3
```

Try borrowing three books for member 1:

```cmd
curl.exe -i -X POST http://localhost:8080/api/borrowings/1/books/101
curl.exe -i -X POST http://localhost:8080/api/borrowings/1/books/102
curl.exe -i -X POST http://localhost:8080/api/borrowings/1/books/103
```

If member 1 is a student and starts with no borrowed books, the third borrowing should be rejected because the student limit is two books.

### 5. Test the Fixed Policy

Set the policy to `fixed` in `application.yml`:

```yaml
library:
  borrowing-policy: fixed
  event-threshold: 3
```

Restart the application. Borrow three available books for a member, then try borrowing a fourth. The fourth borrowing should be rejected.

## Checking Logs

After sending requests, check the IntelliJ IDEA **Run** console for:

- `OperationLoggingAspect` – Borrowing and returning operations and execution times.
- `ExceptionLoggingAspect` – Exceptions intercepted by the custom aspect.
- `BorrowingValidationAspect` – Validation activity, if logged by the aspect.
- `ManyBooksBorrowedListener` – Handling of the borrowing-threshold event.
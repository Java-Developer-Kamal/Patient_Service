# 🏥 Patient Service Microservice

A robust RESTful API built with **Spring Boot 3** and **Java 17** to manage patient demographics, emergency contacts, and feedback history within a healthcare ecosystem.

## 🚀 Key Features

* **Patient Management**: Register new patients with automatic duplicate checks (Email/Phone).
* **Emergency Contacts**: Link multiple emergency contacts to a patient profile.
* **Feedback System**: Submit and view patient satisfaction ratings/comments.
* **Advanced Data Retrieval**: Full support for **Pagination** and **Sorting** on historical data.
* **Standardized API Responses**: Custom `ApiResponse` wrapper for consistent JSON structure (Success/Error).
* **Global Error Handling**: Centralized exception handling (`@ControllerAdvice`) for 400, 404, 409, and 500 errors.
* **Validation**: Strict input validation using Jakarta Validation (Regex for phones, Zip codes, etc.).
* **Documentation**: Integrated **Swagger UI / OpenAPI 3** documentation.

## 🛠️ Tech Stack

* **Core**: Java 17, Spring Boot 3.2.3
* **Database**: MySQL 8.0, Spring Data JPA, Hibernate
* **Tools**: Lombok, MapStruct (DTO Mapping)
* **Testing**: JUnit 5, Mockito, MockMvc
* **Build Tool**: Maven

## 🔌 API Endpoints

### 👤 Patient Controller
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/v1/patients` | Register a new patient |
| `GET` | `/api/v1/patients/{id}` | Get patient profile by ID |

### 📝 Feedback Controller
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/v1/patients/{id}/feedback` | Submit feedback for a patient |
| `GET` | `/api/v1/patients/{id}/feedback` | Get feedback history (Paginated) |

## 🧪 Testing

This project follows industry-standard testing practices:
* **Unit Tests**: Service layer logic tested using `Mockito` and `JUnit 5`.
* **Integration Tests**: Controller layer tested using `MockMvc` and `WebMvcTest`.

To run tests:
```bash
mvn test

# Bookstore RESTful API

> [!NOTE]
> This is a **sample application** created strictly for demonstration purposes. It is designed to showcase **Agentic AI development** workflows on legacy Java/JAX-RS projects and should not be used in production.

A JAX-RS (Jersey) based REST API for a bookstore application.

## Prerequisites
- Java 11 or higher
- Maven 3.6+

## Running the Application

You can run the application locally using the Jetty Maven Plugin:

```bash
mvn jetty:run
```

Once the server is started, the API will be available at: `http://localhost:8080/api/`

### Sample API Endpoints
- **Books**: `http://localhost:8080/api/books`
- **Authors**: `http://localhost:8080/api/authors`
- **Customers**: `http://localhost:8080/api/customers`
- **Orders**: `http://localhost:8080/api/orders`
- **Carts**: `http://localhost:8080/api/carts`

## Development
- The project uses **Jersey** for JAX-RS implementation.
- Data persistence is simulated or handled via repository classes (check `src/main/java/com/bookstore/repository`).
- The server is configured to hot-reload on changes (scan interval: 3 seconds).

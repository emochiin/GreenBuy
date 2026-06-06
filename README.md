# GreenBuy 🌱
 
A mini web shop built in Java — developed as part of a university programming course (Programmieren 2).
 
The focus is on clean architecture, testable code, and idiomatic use of Java Streams.
 
---
 
## Features
 
- **Product catalog** — load products from JSON, filter and sort via Java Streams
- **Customer management** — add, deactivate, and manage customers with shopping cart and purchase history
- **Stream queries** — filter by price, category, sort by name, calculate CO₂ savings, group by category
- **File I/O** — JSON & CSV import/export via Jackson and CSV
- **Console menu** — fully interactive demo via `main()`, no GUI framework
- **Unit tests** — 100% coverage on `ProductService`, mock-based tests for `FileService`
---
 
## Tech Stack
 
| | |
|---|---|
| Language | Java |
| JSON/CSV | Jackson Databind + CSV |
| Testing | JUnit 5 + Mockito |
| Build | Maven |
 
---
 
## Project Structure
 
```
src/
├── main/
│   ├── java/
│   │   ├── Product.java
│   │   ├── Customer.java
│   │   ├── Category.java (Enum)
│   │   ├── ProductService.java
│   │   ├── CustomerService.java
│   │   ├── CustomerStore.java
│   │   ├── FileService.java
│   │   ├── DemoService.java
│   │   ├── Demo.java
│   │   ├── Main.java
│   │   └── IO.java
│   └── resources/
│       └── catalog.json
└── test/
    └── java/
        ├── ProductServiceTest.java
        ├── CustomerTest.java
        └── FileServiceTest.java

```
 
---
 
## Architecture
 
The project follows the **Single Responsibility Principle**:
 
- `Product` / `Customer` — data only, no logic
- `ProductService` — all Stream queries and filters
- `CustomerService` — customer lifecycle (add, deactivate, rename)
- `CustomerStore` — in-memory storage, separates active and inactive customers
- `FileService` — all file I/O operations (JSON read/write)
- `DemoService` / `Demo` — console interaction and menu navigation
Encapsulation is enforced throughout — all fields are `private`, lists are returned as `Collections.unmodifiableList()`, and internal state is only modified via dedicated methods like `addProduct()`.
 
---
 
## Stream Queries (ProductService)
 
```java
filterProductsByPrice(catalog, 2.00)        // all products under a price
filterProductsByCategory(catalog, OBST)     // all products of a category
sortProductsByName(catalog)                  // alphabetically sorted catalog
calculateSavedCo2Value(customer)             // total CO₂ savings for a customer
groupProductsByCategory(catalog)             // grouped by category (Collectors.groupingBy)
```
 
---
 
## Tests
  
`ProductServiceTest` covers all Stream methods including edge cases (empty lists, invalid categories).
`FileServiceTest` uses Mockito to simulate file reads and corrupted input without touching the real filesystem.
`CustomerTest` covers all methods from Customer.java
 
---
 
## Course Context
 
Developed as part of **Programmieren 2** at university. The goal was to apply core Java concepts — Collections, Streams, File I/O, Unit Tests, Mocks, encapsulation, and clean architecture — in a coherent, real-world-inspired project.

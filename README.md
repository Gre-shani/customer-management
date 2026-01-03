# Customer Management System

## Overview

This is a simple Customer Management System built with Spring Boot (backend) and ReactJS (frontend). It supports CRUD operations, pagination, sorting, and bulk customer upload via Excel files.

---

## Technologies Used

* **Backend:** Java 8, Spring Boot, Spring Data JPA, Hibernate
* **Frontend:** ReactJS, Axios
* **Database:** MariaDB
* **Build Tool:** Maven
* **Testing:** JUnit
* **File Processing:** Apache POI for Excel

---

## Features

1. Create, update, view, and delete customers
2. Multiple addresses, mobile numbers, and family members per customer
3. Pagination and sorting of customers in frontend table
4. Bulk customer creation via Excel file upload
5. Minimal DB calls to improve performance

---

## Database Setup

1. Create a MariaDB database, e.g., `customer_db`
2. Run the provided DDL scripts to create tables: `customer`, `customer_address`, `customer_mobile`, `customer_family`, `city`, `country`
3. Insert initial master data for `city` and `country`

---

## Backend Setup

1. Clone the project repository
2. Update `application.properties` with your MariaDB credentials:

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/customer_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

3. Build the project using Maven:

```bash
mvn clean install
```

4. Run the Spring Boot backend:

```bash
mvn spring-boot:run
```

The backend will start on **[http://localhost:8081](http://localhost:8081)**

---

## Frontend Setup

1. Navigate to the `frontend` folder (ReactJS app)
2. Install dependencies:

```bash
npm install
```

3. Start the frontend:

```bash
npm start
```

The frontend will run on **[http://localhost:3000](http://localhost:3000)** and communicate with the backend

---

## Bulk Upload

1. Use the endpoint `/api/customers/bulk` to upload an Excel file containing customer data
2. Excel file requirements:

   * Columns: Name, Date of Birth (yyyy-MM-dd), NIC
   * Date format must match backend expectations
3. The backend processes records in batches to handle large files efficiently

---

## Testing

1. Backend unit tests are written with JUnit
2. Run tests with Maven:

```bash
mvn test
```

---

## Notes

* Ensure `city` and `country` master data exist before creating customers
* Pagination and sorting are available in frontend table view
* The frontend design is simple, elegant, and responsive

---

## Contact

For issues or questions, contact the developer or check the source code documentation.

# ECommerce-App Project

## Overview
This is a comprehensive e-Commerce application built as a set of microservices to manage products, users, orders, and carts. Each service is independent and handles specific functionalities, ensuring scalability and modularity for an excellent online shopping experience.

## Features
- **User Management**: 
  - Registration and Login functionality.
  - Secure authentication using JWT.

- **Product Management**:
  - Add, update, delete, and retrieve products.
  - Category and brand associations.

- **Cart Service**:
  - Add and remove items from the cart.
  - Retrieve cart details for users.

- **Order Service**:
  - Place and manage orders.
  - Track order status and history.
 
- **Analysis & Reporting Service**:
  - Create Sales reports.
  - Get the revenues based on time range.
 
- **Review Rating Service**:
  - Post the review about any product.

- **Global Exception Handling**:
  - Ensures smooth error management across the application.

## Technologies Used
- **Backend**: Spring Boot, Java
- **Authentication**: JWT
- **Database**: MySQL
- **Cache**: Redis
- **Build Tools**: Maven
- **Other Tools**: IntelliJ IDEA, Postman

## Installation

### Prerequisites
- Java 17+
- MySQL Server
- Maven

### Steps to Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/bharathrajb6/Ecommerce-App
   ```

2. Navigate to the project directory:
   ```bash
   cd Ecommerce-App
   ```

3. Configure the database:
   - Create a MySQL database for each microservice (e.g., `user_db`, `product_db`, `order_db`, `cart_db`).
   - Update the database configuration in each service's `application.properties` file:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/<service_db>
     spring.datasource.username=<your-username>
     spring.datasource.password=<your-password>

4. Build the application:
   ```bash
   mvn clean install
   ```

5. Run each microservice:
   ```bash
   mvn spring-boot:run -pl <microservice-name>
   ```

6. Access the application:
   - localhsot:9090

## API Endpoints
### User Service
- `POST /api/users/register`: Register a new user.
- `POST /api/users/login`: Authenticate a user.

### Product Service
- `GET /api/products`: Retrieve all products.
- `POST /api/products`: Add a new product.
- `PUT /api/products/{id}`: Update product details.
- `DELETE /api/products/{id}`: Delete a product.

### Cart Service
- `POST /api/cart`: Add items to the cart.
- `GET /api/cart`: Retrieve cart details.
- `DELETE /api/cart/{id}`: Remove an item from the cart.

### Order Service
- `POST /api/orders`: Place a new order.
- `GET /api/orders`: Retrieve all orders.
- `GET /api/orders/{id}`: Retrieve specific order details.

## Future Enhancements
- Integration with payment gateways.
- Implementation of an advanced search and filter feature for products.
- Frontend development for better user experience.
- Adding a recommendation system based on user preferences.

## Contribution
Contributions are welcome! Please follow the steps below to contribute:
1. Fork the repository.
2. Create a new branch for your feature:
   ```bash
   git checkout -b feature-name
   ```
3. Commit your changes:
   ```bash
   git commit -m "Description of your changes"
   ```
4. Push the changes:
   ```bash
   git push origin feature-name
   ```
5. Create a pull request.
---

Happy Coding!

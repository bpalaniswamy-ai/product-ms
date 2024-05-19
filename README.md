# product-ms

This is a microservice for managing products.
Products are stored in a MySQL database and can be accessed using a REST API.

Pre-requisites:
- Java 17
- MySQL 8.0.27
- Maven 3.9.6

## Usage    

1. Clone the repository
2. Install dependencies using `mvn clean install`
3. Start the server using `mvn spring-boot:run`
4. Access the API using a tool like Postman or curl

## Endpoints    
### GET /products        
Returns a list of all products.

### GET /products/{id}        
Returns a product with the given ID.    

### POST /products        
Creates a new product.    

### PUT /products/{id}        
Updates a product with the given ID.    

### DELETE /products/{id}        
Deletes a product with the given ID.

This microservice uses Spring Boot and Spring Data JPA for database access. All the events are logged using Slf4j.
Entity events are published using Apache Kafka.

Database Configuration:
- URL: jdbc:mysql://localhost:3306/product_db
- Username: root
- Password: password


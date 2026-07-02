# Payment Service Documentation

## Overview
The Payment Service is a microservice that handles payment processing for different payment methods. It uses a **Strategy Pattern** to support multiple payment types and publishes payment success events to **Kafka** for other services to consume.

## Features
- **Multiple Payment Methods**: Supports UPI and CARD payments
- **Strategy Pattern**: Easy to add new payment types without modifying existing code
- **Factory Pattern**: Dynamically selects the appropriate payment strategy
- **Event-Driven**: Publishes payment success events to Kafka for notification services
- **Input Validation**: Validates payment requests before processing
- **Exception Handling**: Centralized error handling with meaningful error messages
- **Async Support**: Enabled for asynchronous processing

## Architecture

### Components

1. **PaymentController** (`/payments`)
   - REST API endpoint that receives payment requests
   - Validates incoming requests
   - Delegates to PaymentService

2. **PaymentService**
   - Core business logic
   - Uses PaymentFactory to get the right payment strategy
   - Processes payment
   - Publishes success event to Kafka

3. **PaymentFactory**
   - Factory pattern implementation
   - Returns the appropriate payment strategy based on payment type
   - Supported types: UPI, CARD

4. **Payment Strategies**
   - `PaymentStrategy` (interface)
   - `UpiPaymentStrategy` - Handles UPI payments
   - `CardPaymentStrategy` - Handles card payments
   - Easy to add new strategies (e.g., PayPal, NetBanking)

5. **PaymentKafkaProducer**
   - Publishes payment success events to Kafka
   - Topic: `payment-success-topic`
   - Used by notification services to send emails/notifications

6. **Exception Handling**
   - `GlobalExceptionHandler` - Centralized error handling
   - `InvalidPaymentTypeException` - Custom exception for invalid payment types

## API Endpoint

### Process Payment

**POST** `/payments`

**Request Body:**
```json
{
  "amount": 100.50,
  "paymentType": "UPI",
  "email": "user@example.com"
}
```

**Request Validation:**
- `amount`: Required, must be greater than 0
- `paymentType`: Required, must be "UPI" or "CARD" (case-insensitive)
- `email`: Required, must be a valid email address

**Success Response (200 OK):**
```json
{
  "transactionId": "UPI123456",
  "status": "SUCCESS",
  "amount": 100.50
}
```

**Error Responses:**

Invalid Payment Type (400 Bad Request):
```json
{
  "message": "Invalid payment type. Use UPI or CARD"
}
```

Validation Error (400 Bad Request):
```json
{
  "amount": "Amount is greater then 0",
  "email": "Enter a valid email address"
}
```

## How It Works

1. **Request Received**: Client sends payment request to `/payments` endpoint
2. **Validation**: Request is validated for required fields and data types
3. **Strategy Selection**: PaymentFactory selects the appropriate payment strategy (UPI or CARD)
4. **Payment Processing**: The selected strategy processes the payment
5. **Event Publishing**: Payment success event is published to Kafka topic
6. **Response**: Payment response with transaction ID and status is returned

```
Client → Controller → Service → Factory → Strategy → Kafka → Response
```

## Technologies Used

- **Java 17** - Programming language
- **Spring Boot 3.5.15** - Application framework
- **Spring Kafka** - Kafka integration
- **Lombok** - Reduce boilerplate code
- **Maven** - Build tool
- **Apache Kafka** - Event streaming platform

## Configuration

### Application Properties (`application.properties`)

```properties
# Application Name
spring.application.name=payment-service

# Kafka Configuration
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer

# Kafka Topic
payment.kafka.topic.payment-success=payment-success-topic

# External Service
notification.service.base-url=http://localhost:8084
```

## Kafka Event Structure

**PaymentSuccessKafkaEvent**:
```json
{
  "transactionId": "UPI123456",
  "amount": 100.50,
  "paymentType": "UPI",
  "email": "user@example.com"
}
```

This event is published to the `payment-success-topic` and can be consumed by:
- Notification Service (to send email confirmations)
- Order Service (to update order status)
- Analytics Service (to track payment metrics)

## How to Run

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Apache Kafka running on localhost:9092

### Using Maven
```bash
./mvnw spring-boot:run
```

### Using JAR
```bash
./mvnw clean package
java -jar target/payment-service-0.0.1-SNAPSHOT.jar
```

### Using Docker
```bash
docker build -t payment-service .
docker run -p 8080:8080 payment-service
```

## Adding New Payment Types

To add a new payment type (e.g., PayPal):

1. **Create a new strategy class:**
```java
@Service
public class PayPalPaymentStrategy implements PaymentStrategy {
    @Override
    public PaymentResponse pay(PaymentRequest request) {
        // Implement PayPal payment logic
        return PaymentResponse.builder()
                .transactionId("PAYPAL123456")
                .status("SUCCESS")
                .amount(request.getAmount())
                .build();
    }
}
```

2. **Update PaymentFactory:**
```java
private final PayPalPaymentStrategy payPalPaymentStrategy;

public PaymentStrategy getPaymentStrategy(String paymentType) {
    if (paymentType.equalsIgnoreCase("PAYPAL")) {
        return payPalPaymentStrategy;
    }
    // existing conditions...
}
```

That's it! No changes needed in Service or Controller.

## Project Structure

```
payment-service/
├── src/
│   ├── main/
│   │   ├── java/com/payment_service/
│   │   │   ├── controller/          # REST controllers
│   │   │   ├── service/             # Business logic
│   │   │   ├── dto/                 # Data transfer objects
│   │   │   ├── factory/             # Factory pattern
│   │   │   ├── strategy/            # Payment strategies
│   │   │   ├── kafka/               # Kafka producer
│   │   │   ├── exception/           # Exception handling
│   │   │   └── PaymentServiceApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
├── Dockerfile
└── README.md
```

## Testing with Postman

### Setup
1. **Start the Payment Service**:
   ```bash
   ./mvnw spring-boot:run
   ```
   Service will be available at `http://localhost:8080`

2. **Ensure Kafka is running**:
   ```bash
   # If using Docker
   docker run -d -p 9092:9092 --name kafka apache/kafka:latest
   ```

### Test Cases

#### Test Case 1: Successful UPI Payment
- **Method**: POST
- **URL**: `http://localhost:8080/payments`
- **Headers**: 
  - `Content-Type: application/json`
- **Body** (raw JSON):
  ```json
  {
    "amount": 500.00,
    "paymentType": "UPI",
    "email": "user@example.com"
  }
  ```
- **Expected Response** (200 OK):
  ```json
  {
    "transactionId": "UPI123456",
    "status": "SUCCESS",
    "amount": 500.00
  }
  ```

#### Test Case 2: Successful CARD Payment
- **Method**: POST
- **URL**: `http://localhost:8080/payments`
- **Headers**: 
  - `Content-Type: application/json`
- **Body** (raw JSON):
  ```json
  {
    "amount": 1500.50,
    "paymentType": "CARD",
    "email": "customer@test.com"
  }
  ```
- **Expected Response** (200 OK):
  ```json
  {
    "transactionId": "CARD123456",
    "status": "SUCCESS",
    "amount": 1500.50
  }
  ```

#### Test Case 3: Invalid Payment Type
- **Method**: POST
- **URL**: `http://localhost:8080/payments`
- **Headers**: 
  - `Content-Type: application/json`
- **Body** (raw JSON):
  ```json
  {
    "amount": 100.00,
    "paymentType": "PAYPAL",
    "email": "user@example.com"
  }
  ```
- **Expected Response** (400 Bad Request):
  ```json
  {
    "message": "Invalid payment type. Use UPI or CARD"
  }
  ```

#### Test Case 4: Missing Amount
- **Method**: POST
- **URL**: `http://localhost:8080/payments`
- **Headers**: 
  - `Content-Type: application/json`
- **Body** (raw JSON):
  ```json
  {
    "paymentType": "UPI",
    "email": "user@example.com"
  }
  ```
- **Expected Response** (400 Bad Request):
  ```json
  {
    "amount": " Amount is required"
  }
  ```

#### Test Case 5: Negative Amount
- **Method**: POST
- **URL**: `http://localhost:8080/payments`
- **Headers**: 
  - `Content-Type: application/json`
- **Body** (raw JSON):
  ```json
  {
    "amount": -50.00,
    "paymentType": "UPI",
    "email": "user@example.com"
  }
  ```
- **Expected Response** (400 Bad Request):
  ```json
  {
    "amount": "Amount is greater then 0"
  }
  ```

#### Test Case 6: Invalid Email
- **Method**: POST
- **URL**: `http://localhost:8080/payments`
- **Headers**: 
  - `Content-Type: application/json`
- **Body** (raw JSON):
  ```json
  {
    "amount": 100.00,
    "paymentType": "UPI",
    "email": "invalid-email"
  }
  ```
- **Expected Response** (400 Bad Request):
  ```json
  {
    "email": "Enter a valid email address"
  }
  ```

#### Test Case 7: Missing Payment Type
- **Method**: POST
- **URL**: `http://localhost:8080/payments`
- **Headers**: 
  - `Content-Type: application/json`
- **Body** (raw JSON):
  ```json
  {
    "amount": 100.00,
    "email": "user@example.com"
  }
  ```
- **Expected Response** (400 Bad Request):
  ```json
  {
    "paymentType": "Payment type is required"
  }
  ```

#### Test Case 8: Case-Insensitive Payment Type
- **Method**: POST
- **URL**: `http://localhost:8080/payments`
- **Headers**: 
  - `Content-Type: application/json`
- **Body** (raw JSON):
  ```json
  {
    "amount": 200.00,
    "paymentType": "upi",
    "email": "user@example.com"
  }
  ```
- **Expected Response** (200 OK):
  ```json
  {
    "transactionId": "UPI123456",
    "status": "SUCCESS",
    "amount": 200.00
  }
  ```

### Creating a Postman Collection

1. **Create Collection**:
   - Click "New" → "Collection"
   - Name it "Payment Service API"

2. **Add Requests**:
   - For each test case above, create a new request
   - Save them in the collection

3. **Environment Variables** (Optional):
   - Create an environment "Payment Service Dev"
   - Add variable: `base_url` = `http://localhost:8080`
   - Use in requests: `{{base_url}}/payments`

4. **Export Collection**:
   - Click the collection → "Export"
   - Share with team members

### Verifying Kafka Events

After successful payment, verify the Kafka event:

```bash
# Consume messages from the payment-success-topic
kafka-console-consumer --bootstrap-server localhost:9092 \
  --topic payment-success-topic \
  --from-beginning
```

You should see events like:
```json
{
  "transactionId": "UPI123456",
  "amount": 500.00,
  "paymentType": "UPI",
  "email": "user@example.com"
}
```

## Default Port
The service runs on port **8080** by default.

## Notes
- Current implementation uses mock transaction IDs for demonstration
- In production, integrate with actual payment gateways (Stripe, Razorpay, etc.)
- Add database persistence for transaction history
- Implement retry logic for Kafka failures
- Add authentication and authorization

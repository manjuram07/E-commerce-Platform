## E-Commerce Platform – Microservices
A scalable E-Commerce Platform built using a microservices architecture with Spring Boot. The system separates authentication, customer management, product catalog, cart management, payments, and API routing into independently deployable services.

## Architecture Overview
```
                         +----------------------+
                         |      Frontend        |
                         | Web / Mobile Client  |
                         +----------+-----------+
                                    |
                                    v
                         +----------------------+
                         |     API Gateway      |
                         | Single Entry Point   |
                         +----------+-----------+
                                    |
          +-------------------------+-------------------------+
          |            |             |          |             |
          v            v             v          v             v
 +---------------+ +---------+ +-----------+ +---------+ +-----------+
 | Auth Service  | |Customer | | Product   | | Cart    | | Payment   |
 | AuthN/AuthZ   | |Service  | | Service   | |Service  | | Service   |
 +-------+-------+ +----+----+ +-----+-----+ +----+----+ +-----+-----+
         |              |            |            |             |
         v              v            v            v             v
    +---------+    +---------+  +---------+  +---------+   +---------+
    | Auth DB |    |Customer |  |Product  |  | Cart DB |   |Payment  |
    |         |    | DB      |  | DB      |  |         |   | DB      |
    +---------+    +---------+  +---------+  +---------+   +---------+

```

### Services

| Service | Responsibility | Primary Data |
|---|---|---|
| API Gateway | Single entry point, routing, cross-cutting concerns | No business database |
| Auth Service | Authentication, authorization, users, roles, JWT | Users, roles, credentials/identity data |
| Customer Service | Customer profile and account information | Customers, addresses |
| Product Service | Product catalog, inventory, pricing | Products, categories, inventory |
| Cart Service | Shopping cart and cart items | Carts, cart items |
| Payment Service | Payment initiation, validation, status tracking | Payments, payment transactions |




## 1. Service Responsibilities

### 1.1 API Gateway

The API Gateway is the single public entry point for client applications.

Responsibilities

Route requests to the appropriate microservice.

Validate and propagate authentication information.

Apply common security policies.

Handle CORS and request filtering.

Provide centralized logging and request tracing.

Apply rate limiting where required.

Hide internal service addresses from external clients.

Provide a consistent external API boundary.

Example Routes

/api/v1/auth/**       -> Auth Service
/api/v1/customers/**  -> Customer Service
/api/v1/products/**   -> Product Service
/api/v1/cart/**       -> Cart Service
/api/v1/payments/**   -> Payment Service

### 1.2 Auth Service

Responsible for authentication and authorization across the platform.

Responsibilities

User registration and login.

Password hashing and credential management.

JWT generation and validation.

Role-based authorization.

Refresh-token management where applicable.

User account status management.

Security-related audit information.

Typical Roles

CUSTOMER
ADMIN

### 1.3 Customer Service

Manages customer-specific business information independently from authentication data.

Responsibilities

Customer profile management.

Customer contact information.

Shipping and billing addresses.

Customer preferences.

Customer account lifecycle.

Expose customer information required by other services through APIs.

Authentication credentials remain owned by the Auth Service. The Customer Service owns business/customer profile data.

### 1.4 Product Service

Responsible for the product catalog and inventory-related information.

Responsibilities

Product creation and updates.

Product retrieval and search.

Category management.

Product pricing.

Inventory availability.

Product activation/deactivation.

Product metadata such as SKU, description, brand, and images.

### 1.5 Cart Service

Responsible for the customer's active shopping cart.

Responsibilities

Create and retrieve carts.

Add products to a cart.

Update item quantities.

Remove items.

Clear carts.

Calculate cart subtotal.

Validate product availability through Product Service.

Associate carts with authenticated customers.

The Cart Service should not become the source of truth for product price or inventory. Product Service remains the owner of product information.

### 1.6 Payment Service

Responsible for payment processing and payment lifecycle management.

Responsibilities

Initiate payments.

Validate payment requests.

Apply payment business rules.

Integrate with an external payment provider where required.

Maintain payment status.

Handle retries and idempotency.

Store payment transaction references.

Publish or expose payment status for downstream processing.

Typical payment lifecycle:
```
INITIATED -> PROCESSING -> SUCCESS
                    |
                    +----> FAILED
```

# API Design – Endpoints

All external APIs are exposed through the **API Gateway** using a versioned REST API.

## Base URL

```text
http://localhost:8080/api/v1
```

---

## 2.1 Auth Service APIs

| Method | Endpoint                     | Description                              | Access        |
| ------ | ---------------------------- | ---------------------------------------- | ------------- |
| `POST` | `/auth/register`             | Register a new user                      | Public        |
| `POST` | `/auth/login`                | Authenticate user and issue access token | Public        |
| `POST` | `/auth/refresh-token`        | Generate a new access token              | Authenticated |
| `POST` | `/auth/logout`               | Invalidate session/refresh token         | Authenticated |
| `GET`  | `/auth/me`                   | Get authenticated user information       | Authenticated |
| `PUT`  | `/auth/users/{userId}/roles` | Update user roles                        | Admin         |

### Login Request

```json
{
  "email": "customer@example.com",
  "password": "Password@123"
}
```

### Login Response

```json
{
  "accessToken": "<jwt-token>",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

---

## 2.2 Customer Service APIs

| Method   | Endpoint                                        | Description                    | Access        |
| -------- | ----------------------------------------------- | ------------------------------ | ------------- |
| `POST`   | `/customers`                                    | Create customer profile        | Authenticated |
| `GET`    | `/customers/{customerId}`                       | Get customer profile           | Owner/Admin   |
| `GET`    | `/customers/me`                                 | Get current customer's profile | Authenticated |
| `PUT`    | `/customers/{customerId}`                       | Update customer profile        | Owner/Admin   |
| `DELETE` | `/customers/{customerId}`                       | Deactivate customer            | Owner/Admin   |
| `POST`   | `/customers/{customerId}/addresses`             | Add address                    | Owner/Admin   |
| `GET`    | `/customers/{customerId}/addresses`             | List customer addresses        | Owner/Admin   |
| `PUT`    | `/customers/{customerId}/addresses/{addressId}` | Update address                 | Owner/Admin   |
| `DELETE` | `/customers/{customerId}/addresses/{addressId}` | Delete address                 | Owner/Admin   |

---

## 2.3 Product Service APIs

| Method   | Endpoint                          | Description           | Access               |
| -------- | --------------------------------- | --------------------- | -------------------- |
| `POST`   | `/products`                       | Create product        | Admin                |
| `GET`    | `/products/{productId}`           | Get product details   | Public               |
| `GET`    | `/products`                       | Search/list products  | Public               |
| `PUT`    | `/products/{productId}`           | Update product        | Admin                |
| `DELETE` | `/products/{productId}`           | Deactivate product    | Admin                |
| `POST`   | `/products/{productId}/inventory` | Update inventory      | Admin                |
| `GET`    | `/products/{productId}/inventory` | Get product inventory | Public/Authenticated |
| `GET`    | `/categories`                     | List categories       | Public               |
| `POST`   | `/categories`                     | Create category       | Admin                |

### Product Search Example

```http
GET /api/v1/products?category=electronics&page=0&size=20&sort=price,asc
```

---

## 2.4 Cart Service APIs

| Method   | Endpoint                         | Description                    | Access        |
| -------- | -------------------------------- | ------------------------------ | ------------- |
| `POST`   | `/carts`                         | Create or retrieve active cart | Authenticated |
| `GET`    | `/carts/{cartId}`                | Get cart details               | Owner         |
| `GET`    | `/carts/me`                      | Get current customer's cart    | Authenticated |
| `POST`   | `/carts/{cartId}/items`          | Add item to cart               | Owner         |
| `PUT`    | `/carts/{cartId}/items/{itemId}` | Update item quantity           | Owner         |
| `DELETE` | `/carts/{cartId}/items/{itemId}` | Remove item from cart          | Owner         |
| `DELETE` | `/carts/{cartId}/items`          | Clear cart                     | Owner         |

### Add Cart Item Request

```json
{
  "productId": 101,
  "quantity": 2
}
```

---

## 2.5 Payment Service APIs

| Method | Endpoint                       | Description                       | Access            |
| ------ | ------------------------------ | --------------------------------- | ----------------- |
| `POST` | `/payments`                    | Create/initiate payment           | Authenticated     |
| `GET`  | `/payments/{paymentId}`        | Get payment details               | Owner/Admin       |
| `GET`  | `/payments/{paymentId}/status` | Get payment status                | Owner/Admin       |
| `POST` | `/payments/{paymentId}/retry`  | Retry failed payment              | Owner             |
| `POST` | `/payments/webhook`            | Receive payment provider callback | Internal/Provider |

### Payment Request

```json
{
  "orderReference": "ORD-10001",
  "amount": 2499.00,
  "currency": "INR",
  "paymentMethod": "CARD"
}
```

### Idempotency

Payment creation should support an **`Idempotency-Key`** header to prevent duplicate payments when clients retry requests.

Example:

```http
POST /api/v1/payments
Authorization: Bearer <jwt-token>
Idempotency-Key: 8b7f0c76-8c59-4b39-8ca3-123456789abc
Content-Type: application/json
```

The same idempotency key should produce the same logical payment result rather than creating a second transaction.

---

## API Response Standards

All APIs should use consistent HTTP status codes and error responses.

### Common HTTP Status Codes

| Status Code                 | Meaning                                      |
| --------------------------- | -------------------------------------------- |
| `200 OK`                    | Request completed successfully               |
| `201 Created`               | Resource successfully created                |
| `202 Accepted`              | Request accepted for asynchronous processing |
| `204 No Content`            | Request successful with no response body     |
| `400 Bad Request`           | Invalid request                              |
| `401 Unauthorized`          | Authentication required or invalid           |
| `403 Forbidden`             | User does not have permission                |
| `404 Not Found`             | Resource not found                           |
| `409 Conflict`              | Resource/state conflict                      |
| `422 Unprocessable Entity`  | Business validation failure                  |
| `429 Too Many Requests`     | Rate limit exceeded                          |
| `500 Internal Server Error` | Unexpected server error                      |
| `503 Service Unavailable`   | Service/dependency unavailable               |

### Standard Error Response

```json
{
  "timestamp": "2026-08-16T14:30:00Z",
  "status": 409,
  "code": "PAYMENT_ALREADY_PROCESSED",
  "message": "The payment has already been processed.",
  "path": "/api/v1/payments",
  "correlationId": "7a1f2d9b-1234-4567-8901-abcdef123456"
}
```

---

## API Security

All protected APIs require authentication.

```http
Authorization: Bearer <jwt-token>
```

The API Gateway can enforce:

* JWT validation
* Authentication
* Rate limiting
* Request routing
* Correlation ID propagation
* Circuit breaking
* Request tracing

Individual services remain responsible for **business-level authorization**, such as verifying that a customer owns the requested resource.

---

## API Versioning

The API uses URI-based versioning:

```text
/api/v1/...
```

Examples:

```text
/api/v1/auth/login
/api/v1/customers/{customerId}
/api/v1/products
/api/v1/carts/{cartId}
/api/v1/payments
```

When a breaking API change is required, a new version can be introduced:

```text
/api/v2/...
```

---

## API Request Flow

```text
Client
  |
  v
API Gateway
  |
  +--> Authentication / Authorization
  |
  +--> Rate Limiting
  |
  +--> Routing
  |
  v
Target Microservice
  |
  +--> Business Validation
  |
  +--> Database / External Service
  |
  v
API Response
```

For asynchronous operations such as payment processing:

```text
Client
  |
  v
API Gateway
  |
  v
Payment Service
  |
  +--> Validate Request
  |
  +--> Persist Payment
  |
  +--> Publish Event
  |
  v
Payment Provider
  |
  v
Webhook
  |
  v
Payment Service
  |
  v
Update Payment Status
```













Happy coding! 😊

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














Happy coding! 😊

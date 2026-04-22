# EKart-FA4-Project

[![E-Commerce Payment Flow with Retry, Circuit Breaker, and Fallback](attachment:e-commerce-payment-flow-retry-circuitbreaker-fallback.png)
]()
## Resilience Patterns: @Retry and @CircuitBreaker

This project demonstrates the use of Resilience4j annotations `@Retry` and `@CircuitBreaker` to handle failures gracefully in microservice communications.

### @Retry Annotation

The `@Retry` annotation automatically retries failed operations a specified number of times before giving up. This is useful for transient failures like network timeouts or temporary service unavailability.

#### Scenario: Fetching Product Details from ProductMS

In the `CustomerCartServiceImpl.java`, the `getProductsFromCart` method uses `@Retry` to fetch product details from the ProductMS microservice:

```java
@Retry(name = "ProductMS", fallbackMethod = "fallbackGetProductsFromCart")
@CircuitBreaker(name = "ProductMS", fallbackMethod = "fallbackGetProductsFromCart")
@Override
public Set<CartProductDTO> getProductsFromCart(String customerEmailId) throws EKartCustomerCartException {
    // ... code to retrieve cart products ...
    
    for (CartProductDTO cartProductDTO : cartProductsDTO) {
        ProductDTO productDTO = webClient.get()
            .uri("http://localhost:3334/Ekart/product-api/product/" + cartProductDTO.getProduct().getProductId())
            .retrieve()
            .bodyToMono(ProductDTO.class)
            .block();
        
        cartProductDTO.setProduct(productDTO);
    }
    
    return cartProductsDTO;
}
```

**Scenario Example:**
- User requests to view their cart.
- CartMS calls ProductMS to get product details for each item in the cart.
- If ProductMS is temporarily slow (e.g., high load), the call might timeout.
- `@Retry` will attempt the call multiple times (default is 3 attempts) before falling back to the `fallbackGetProductsFromCart` method.
- This ensures better user experience by handling intermittent network issues.

### @CircuitBreaker Annotation

The `@CircuitBreaker` annotation prevents cascading failures by stopping calls to a failing service after a threshold of failures is reached. It allows the service to recover and prevents overwhelming the failing service.

#### Scenario: Protecting Against ProductMS Outages

The same method also uses `@CircuitBreaker` to protect against prolonged outages of ProductMS:

**Scenario Example:**
- ProductMS experiences a database failure and becomes unresponsive.
- Without circuit breaker, CartMS would keep making calls, wasting resources and potentially causing CartMS to fail as well.
- `@CircuitBreaker` monitors the failure rate:
  - If failure rate exceeds threshold (e.g., 50% failures in last 10 calls), circuit opens.
  - In open state, calls to ProductMS are immediately routed to fallback method.
  - After a timeout period, circuit goes to half-open state to test if ProductMS has recovered.
  - If successful, circuit closes; if not, it opens again.
- This prevents CartMS from being overwhelmed and allows ProductMS time to recover.

### Fallback Method

Both annotations use the same fallback method `fallbackGetProductsFromCart`:

```java
public Set<CartProductDTO> fallbackGetProductsFromCart(String customerEmailId, Throwable t) {
    // Log the exception
    System.err.println("Fallback triggered due to: " + t.getMessage());
    
    // Return cart products with basic info (productId only)
    // ... implementation returns cart data without product details ...
}
```

**Scenario Example:**
- During ProductMS downtime, users can still see their cart contents (quantities, product IDs) even without full product details.
- This provides a degraded but functional service instead of complete failure.

### Configuration

These patterns are configured in `application.yml` or via properties. Default configurations can be customized:

```yaml
resilience4j.retry:
  instances:
    ProductMS:
      maxAttempts: 3
      waitDuration: 100ms

resilience4j.circuitbreaker:
  instances:
    ProductMS:
      failureRateThreshold: 50
      waitDurationInOpenState: 10000ms
      slidingWindowSize: 10
```

### Benefits

1. **Improved Reliability**: Handles transient failures gracefully.
2. **Fault Isolation**: Prevents one service failure from cascading to others.
3. **Resource Protection**: Avoids wasting resources on failing services.
4. **Graceful Degradation**: Provides partial functionality during outages.
5. **Automatic Recovery**: Services can recover without manual intervention.

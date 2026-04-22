package com.infy.ekart.payment.service;

import com.infy.ekart.payment.dto.TransactionStatus;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PaymentCircuitBreakerService {

	private static final Logger log = LoggerFactory.getLogger(PaymentCircuitBreakerService.class);

	private final WebClient webClient;

	@Value("${customer-service.base-url}")
	private String customerServiceBaseUrl;

	public PaymentCircuitBreakerService(WebClient webClient) {
		this.webClient = webClient;
	}

	@CircuitBreaker(name = "paymentService", fallbackMethod = "paymentFailed")
	@Retry(name = "paymentService")
	public void updateOrderAfterPayment(Integer orderId, String transactionStatus) {
		webClient.put()
				.uri(customerServiceBaseUrl + "/order/" + orderId + "/update/order-status")
				.retrieve()
				.bodyToMono(TransactionStatus.class)
				.block(); // ← subscribes and triggers the actual HTTP call
	}

	public void paymentFailed(Integer orderId, String transactionStatus, Exception ex) {
		log.warn("Fallback: could not update order {} with status {}. Cause: {}",
				orderId, transactionStatus, ex.getMessage());
	}
}
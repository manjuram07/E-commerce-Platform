package com.infy.ekart.payment.api;

import java.security.NoSuchAlgorithmException;

import java.util.List;


import com.infy.ekart.payment.service.KafkaProducerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.WebClient;

import com.infy.ekart.payment.dto.CardDTO;
import com.infy.ekart.payment.dto.OrderDTO;
import com.infy.ekart.payment.dto.TransactionDTO;
import com.infy.ekart.payment.exception.EKartPaymentException;
import com.infy.ekart.payment.exception.PayOrderFallbackException;
//import com.infy.ekart.payment.service.PaymentCircuitBreakerService;
import com.infy.ekart.payment.service.PaymentService;

@CrossOrigin
@RestController
@RequestMapping(value = "/payment-api")
@Validated
public class PaymentAPI {

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private Environment environment;
	
//	@Autowired
//	private PaymentCircuitBreakerService paymentCircuitBreakerService;

	@Autowired
	private WebClient webClient;

	private static final Log logger = LogFactory.getLog(PaymentAPI.class);

	@Autowired
	private KafkaProducerService producer;

	@PostMapping("/publish")
	public String publish(@RequestParam String message) {
		logger.info("Publishing message to Kafka topic: " + message);
		producer.sendMessage("my-topic", message);
		return "Message sent: " + message;
	}

	@PostMapping(value = "/customer/{customerEmailId:.+}/cards")
	public ResponseEntity<String> addNewCard(@RequestBody CardDTO cardDTO,
			@Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") @PathVariable("customerEmailId") String customerEmailId)
			throws EKartPaymentException, NoSuchAlgorithmException {
		logger.info("Recieved request to add new  card for customer : " + cardDTO.getCustomerEmailId());

		int cardId;
		cardId = paymentService.addCustomerCard(customerEmailId, cardDTO);
		String message = environment.getProperty("PaymentAPI.NEW_CARD_ADDED_SUCCESS");
		String toReturn = message + cardId;
//		toReturn = toReturn.trim();
		return new ResponseEntity<>(toReturn, HttpStatus.OK);

	}

	@PutMapping(value = "/update/card")
	public ResponseEntity<String> updateCustomerCard(@Valid @RequestBody CardDTO cardDTO)
			throws EKartPaymentException, NoSuchAlgorithmException {
		logger.info("Recieved request to update  card :" + cardDTO.getCardId() + " of customer : "
				+ cardDTO.getCustomerEmailId());

		paymentService.updateCustomerCard(cardDTO);
		String modificationSuccessMsg = environment.getProperty("PaymentAPI.UPDATE_CARD_SUCCESS");
		return new ResponseEntity<>(modificationSuccessMsg, HttpStatus.OK);

	}

	@DeleteMapping(value = "/customer/{customerEmailId:.+}/card/{cardID}/delete")
	public ResponseEntity<String> deleteCustomerCard(@PathVariable("cardID") Integer cardID,
			@Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") @PathVariable("customerEmailId") String customerEmailId)
			throws EKartPaymentException {
		logger.info("Recieved request to delete  card :" + cardID + " of customer : " + customerEmailId);

		paymentService.deleteCustomerCard(customerEmailId, cardID);
		String modificationSuccessMsg = environment.getProperty("PaymentAPI.CUSTOMER_CARD_DELETED_SUCCESS");
		return new ResponseEntity<>(modificationSuccessMsg, HttpStatus.OK);

	}

	@GetMapping(value = "/customer/{customerEmailId}/card-type/{cardType}")
	public ResponseEntity<List<CardDTO>> getCardsOfCustomer(@PathVariable String customerEmailId,
			@PathVariable String cardType) throws EKartPaymentException {
		logger.info("Recieved request to fetch  cards of customer : " + customerEmailId + " having card type as: "
				+ cardType);

		List<CardDTO> cardDTOs = paymentService.getCustomerCardOfCardType(customerEmailId, cardType);
		return new ResponseEntity<>(cardDTOs, HttpStatus.OK);
	}

	// Annotate this method for handling resilience
	// Get the order details from CustomerMS for the given orderId (available in TransactionDTO)
	// Update the Transaction details with the obtained Order details in above step, along with transaction date and total price 
	// Authenticate the transaction details for the given customer by calling authenticatePayment() method of PaymentService
	// Add the transaction details to the database by calling addTransaction() method of PaymentService
	// Update the order status by calling updateOrderAfterPayment() method of PaymentCircuitBreakerService
	// Set the appropriate success or failure message and return the same
	
	//@CircuitBreaker(name="paymentService", fallbackMethod="payForOrderFallback")
	@PostMapping(value = "/customer/{customerEmailId}/pay-order")
	public ResponseEntity<String> payForOrder(
			@Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") @PathVariable("customerEmailId") String customerEmailId,
			@Valid @RequestBody TransactionDTO transactionDTO)
			throws NoSuchAlgorithmException, EKartPaymentException, PayOrderFallbackException {
			
			//write your logic here
		//ResponseEntity<OrderDTO> orderDetails= template.getForEntity("http://CustomerMS/Ekart/customerorder-api/order"+transactionDTO.getOrder().getOrderId(),OrderDTO.class);
		
		OrderDTO orderDetails = webClient
										.get().uri("http://CustomerMS/Ekart/customerorder-api/order"+transactionDTO.getOrder().getOrderId())
										.retrieve()
										.bodyToMono(OrderDTO.class)
										.block();
		
		transactionDTO.setTransactionDate(orderDetails.getDateOfOrder());
		transactionDTO.setTotalPrice(orderDetails.getTotalPrice());
		paymentService.authenticatePayment(customerEmailId, transactionDTO);
		paymentService.addTransaction(transactionDTO);
		//paymentCircuitBreakerService.updateOrderAfterPayment(transactionDTO.getOrder().getOrderId(), transactionDTO.getTransactionStatus().toString());
		String message= environment.getProperty("PaymentAPI.TRANSACTION_SUCCESSFULL_ONE")+transactionDTO.getTotalPrice()+environment.getProperty("PaymentAPI.TRANSACTION_SUCCESSFULL_TWO")+transactionDTO.getOrder().getOrderId()+environment.getProperty("PaymentAPI.TRANSACTION_SUCCESSFULL_THREE")+transactionDTO.getTransactionId();
	
			return new ResponseEntity<>(message,HttpStatus.OK);

	}
	
	//Implement a fallback method here
    //If exception message is Payment.TRANSACTION_FAILED_CVV_NOT_MATCHING then set message as Payment.TRANSACTION_FAILED_CVV_NOT_MATCHING
	//Else if exception message contains "Order not found" then re-throw exception as RestClientException (with message same as exception message)
	//Else set the message as PaymentAPI.PAYMENT_FAILURE_FALLBACK
	//Return the above message as response
	public ResponseEntity<String> payForOrderFallback(String customerEmailId, TransactionDTO transactionDTO,
			RuntimeException exception) {
		//write your logic here
		String message="";
		if(exception.getMessage().equals("Payment.TRANSACTION_FAILED_CVV_NOT_MATCHING"))
			message= environment.getProperty( "Payment.TRANSACTION_FAILED_CVV_NOT_MATCHING");
		else if(exception.getMessage().contains("Order not found"))
			throw new RestClientException(exception.getMessage());
		else
			message = environment.getProperty("PaymentAPI.PAYMENT_FAILURE_FALLBACK");
		return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
	}
}

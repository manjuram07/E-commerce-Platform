package com.infy.ekart.cart.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.infy.ekart.cart.dto.CartProductDTO;
import com.infy.ekart.cart.dto.CustomerCartDTO;
import com.infy.ekart.cart.dto.ProductDTO;
import com.infy.ekart.cart.entity.CartProduct;
import com.infy.ekart.cart.entity.CustomerCart;
import com.infy.ekart.cart.exception.EKartCustomerCartException;
import com.infy.ekart.cart.repository.CartProductRepository;
import com.infy.ekart.cart.repository.CustomerCartRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service(value = "customerCartService")
@Transactional
public class CustomerCartServiceImpl implements CustomerCartService {

    @Autowired
    private CustomerCartRepository customerCartRepository;

    @Autowired
    private CartProductRepository cartProductRepository;
    
	@Autowired
	private WebClient webClient;


    // this method adds new products in to the customer cart
    @Override
    public Integer addProductToCart(CustomerCartDTO customerCartDTO) throws EKartCustomerCartException {
        Set<CartProduct> cartProducts = new HashSet<>();
        Integer cartId = null;
        for (CartProductDTO cartProductDTO : customerCartDTO.getCartProducts()) {
            CartProduct cartProduct = new CartProduct();
            cartProduct.setProductId(cartProductDTO.getProduct().getProductId());
            cartProduct.setQuantity(cartProductDTO.getQuantity());
            cartProducts.add(cartProduct);
        }
        Optional<CustomerCart> cartOptional = customerCartRepository.findByCustomerEmailId(customerCartDTO.getCustomerEmailId());
        if (cartOptional.isEmpty()) {
            CustomerCart newCart = new CustomerCart();
            newCart.setCustomerEmailId(customerCartDTO.getCustomerEmailId());
            newCart.setCartProducts(cartProducts);
            customerCartRepository.save(newCart);
            cartId = newCart.getCartId();
        } else {
            CustomerCart cart = cartOptional.get();
            for (CartProduct cartProductToBeAdded : cartProducts) {
                Boolean checkProductAlreadyPresent = false;
                for (CartProduct cartProductFromCart : cart.getCartProducts()) {
                    if (cartProductFromCart.equals(cartProductToBeAdded)) {

                        cartProductFromCart.setQuantity(cartProductToBeAdded.getQuantity() + cartProductFromCart.getQuantity());
                        checkProductAlreadyPresent = true;
                    }
                }
                if (checkProductAlreadyPresent == false) {
                    cart.getCartProducts().add(cartProductToBeAdded);
                }
            }

            cartId = cart.getCartId();
        }
        return cartId;
    }

    // this method retrieves customer data from repository and returns cart details
    // of that customer
    @Retry(name = "ProductMS", fallbackMethod = "fallbackGetProductsFromCart")
	@CircuitBreaker(name = "ProductMS", fallbackMethod = "fallbackGetProductsFromCart")
    @Override
    public Set<CartProductDTO> getProductsFromCart(String customerEmailId) throws EKartCustomerCartException {
        Optional<CustomerCart> cartOptional = customerCartRepository.findByCustomerEmailId(customerEmailId);
        Set<CartProductDTO> cartProductsDTO = new HashSet<>();
        CustomerCart cart = cartOptional
                .orElseThrow(() -> new EKartCustomerCartException("CustomerCartService.NO_CART_FOUND"));

        if (cart.getCartProducts().isEmpty()) {
            throw new EKartCustomerCartException("CustomerCartService.NO_PRODUCT_ADDED_TO_CART");
        }
        Set<CartProduct> cartProducts = cart.getCartProducts();
        for (CartProduct cartProduct : cartProducts) {
            CartProductDTO cartProductDTO = new CartProductDTO();
            cartProductDTO.setCartProductId(cartProduct.getCartProductId());
            cartProductDTO.setQuantity(cartProduct.getQuantity());
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductId(cartProduct.getProductId());
            cartProductDTO.setProduct(productDTO);
            cartProductsDTO.add(cartProductDTO);
        }
for (CartProductDTO cartProductDTO : cartProductsDTO) {

			// We are calling the product API using hard-coded URI
			// Replace this call with the appropriate MS name
			// Product API is upscaled (available in 2 numbers). Hence, use load balanced
			// template to make call to the Product API


			ProductDTO productDTO = webClient.get()
//																			.uri("http://localhost:3334/product-api/product/" + cartProductDTO.getProduct().getProductId())
																			.uri("http://localhost:3334/Ekart/product-api/product/" + cartProductDTO.getProduct().getProductId())
																			.retrieve()
																			.bodyToMono(ProductDTO.class)
																			.block();		// synchronous because we used block()

//			Mono<ProductDTO> bodyToMono = webClient
//																		 .get()
//																	     .uri("http://localhost:3334/product-api/product/" + orderedProductDTO.getProduct().getProductId())
//																		.retrieve()
//																		.bodyToMono(ProductDTO.class);   // Asynchronous in nature

			cartProductDTO.setProduct(productDTO);

		}
        return cartProductsDTO;
    }

    // this method retrieves customer data from repository and returns cart details
    // of that customer
    @Override
    public void deleteProductFromCart(String customerEmailId, Integer productId) throws EKartCustomerCartException {

        Optional<CustomerCart> cartOptional = customerCartRepository.findByCustomerEmailId(customerEmailId);
        CustomerCart cart = cartOptional
                .orElseThrow(() -> new EKartCustomerCartException("CustomerCartService.NO_CART_FOUND"));
        if (cart.getCartProducts().isEmpty()) {
            throw new EKartCustomerCartException("CustomerCartService.NO_PRODUCT_ADDED_TO_CART");
        }
        CartProduct selectedProduct = null;
        for (CartProduct product : cart.getCartProducts()) {
            if (product.getProductId().equals(productId)) {
                selectedProduct = product;

            }
        }
        if (selectedProduct == null) {
            throw new EKartCustomerCartException("CustomerCartService.PRODUCT_ALREADY_NOT_AVAILABLE");
        }

        cart.getCartProducts().remove(selectedProduct);
        cartProductRepository.delete(selectedProduct);
    }

    @Override
    public void deleteAllProductsFromCart(String customerEmailId) throws EKartCustomerCartException {
        Optional<CustomerCart> cartOptional = customerCartRepository.findByCustomerEmailId(customerEmailId);
        CustomerCart cart = cartOptional
                .orElseThrow(() -> new EKartCustomerCartException("CustomerCartService.NO_CART_FOUND"));

        if (cart.getCartProducts().isEmpty()) {
            throw new EKartCustomerCartException("CustomerCartService.NO_PRODUCT_ADDED_TO_CART");
        }
        List<Integer> productIds = new ArrayList<>();
        cart.getCartProducts().parallelStream().forEach(cp -> {
            productIds.add(cp.getCartProductId());
            cart.getCartProducts().remove(cp);
        });

        productIds.forEach(pid -> {
            cartProductRepository.deleteById(pid);
        });

    }

    @Override
    public void modifyQuantityOfProductInCart(String customerEmailId, Integer productId, Integer quantity)
            throws EKartCustomerCartException {

        Optional<CustomerCart> cartOptional = customerCartRepository.findByCustomerEmailId(customerEmailId);
        CustomerCart cart = cartOptional
                .orElseThrow(() -> new EKartCustomerCartException("CustomerCartService.NO_CART_FOUND"));

        if (cart.getCartProducts().isEmpty()) {
            throw new EKartCustomerCartException("CustomerCartService.NO_PRODUCT_ADDED_TO_CART");
        }
        CartProduct selectedProduct = null;
        for (CartProduct product : cart.getCartProducts()) {
            if (product.getProductId().equals(productId)) {
                selectedProduct = product;
            }
        }
        if (selectedProduct == null) {
            throw new EKartCustomerCartException("CustomerCartService.PRODUCT_ALREADY_NOT_AVAILABLE");
        }
        selectedProduct.setQuantity(quantity);
    }
    
    
    
    
    // Fallback methods 
    public Set<CartProductDTO> fallbackGetProductsFromCart(String customerEmailId, Throwable t) {
	    // Log or handle the exception gracefully
	    System.err.println("Fallback triggered due to: " + t.getMessage());

	    // Return cart products with productId only, without fetching details from ProductMS
	    try {
	        Optional<CustomerCart> cartOptional = customerCartRepository.findByCustomerEmailId(customerEmailId);
	        Set<CartProductDTO> cartProductsDTO = new HashSet<>();
	        CustomerCart cart = cartOptional
	                .orElseThrow(() -> new EKartCustomerCartException("CustomerCartService.NO_CART_FOUND"));

	        if (cart.getCartProducts().isEmpty()) {
	            throw new EKartCustomerCartException("CustomerCartService.NO_PRODUCT_ADDED_TO_CART");
	        }
	        Set<CartProduct> cartProducts = cart.getCartProducts();
	        for (CartProduct cartProduct : cartProducts) {
	            CartProductDTO cartProductDTO = new CartProductDTO();
	            cartProductDTO.setCartProductId(cartProduct.getCartProductId());
	            cartProductDTO.setQuantity(cartProduct.getQuantity());
	            ProductDTO productDTO = new ProductDTO();
	            productDTO.setProductId(cartProduct.getProductId());
	            // Set other fields to null or default, since we can't fetch
	            cartProductDTO.setProduct(productDTO);
	            cartProductsDTO.add(cartProductDTO);
	        }
	        return cartProductsDTO;
	    } catch (EKartCustomerCartException e) {
	        // If cart not found, return empty
	        return Collections.emptySet();
	    }
	}
}

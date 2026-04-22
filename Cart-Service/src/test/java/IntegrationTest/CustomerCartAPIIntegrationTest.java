package IntegrationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infy.ekart.cart.dto.CartProductDTO;
import com.infy.ekart.cart.dto.CustomerCartDTO;
import com.infy.ekart.cart.dto.ProductDTO;

/**
 * Integration Test Cases for CustomerCartAPI class
 * Tests all REST endpoints of the CustomerCartAPI with real Spring context
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@DisplayName("CustomerCartAPI Integration Tests")
public class CustomerCartAPIIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomerCartDTO customerCartDTO;
    private CartProductDTO cartProductDTO;
    private ProductDTO productDTO;
    private static final String BASE_API_PATH = "/Ekart/customercart-api";
    private static final String VALID_EMAIL = "testuser@example.com";
    private static final String INVALID_EMAIL = "invalidemail";
    private static final String INVALID_EMAIL_FORMAT = "user@.com";

    @BeforeEach
    public void setUp() {
        // Initialize ProductDTO
        productDTO = new ProductDTO();
        productDTO.setProductId(1);
        productDTO.setName("Test Product");
        productDTO.setDescription("Test Product Description");
        productDTO.setCategory("Electronics");
        productDTO.setBrand("TestBrand");
        productDTO.setPrice(100.0);
        productDTO.setAvailableQuantity(10);

        // Initialize CartProductDTO
        cartProductDTO = new CartProductDTO();
        cartProductDTO.setCartProductId(1);
        cartProductDTO.setProduct(productDTO);
        cartProductDTO.setQuantity(2);

        // Initialize CustomerCartDTO
        customerCartDTO = new CustomerCartDTO();
        customerCartDTO.setCustomerEmailId(VALID_EMAIL);
        Set<CartProductDTO> cartProducts = new HashSet<>();
        cartProducts.add(cartProductDTO);
        customerCartDTO.setCartProducts(cartProducts);
    }

    // ======================== POST - Add Product to Cart Tests ========================

    /**
     * Test Case 1: Successfully add a product to cart
     * Endpoint: POST /customercart-api/products
     * Expected: HTTP 201 CREATED with success message and cart ID
     */
    @Test
    @DisplayName("Test 1: Add Product to Cart - Success")
    public void testAddProductToCart_Success() throws Exception {
        // Arrange
        String jsonRequest = objectMapper.writeValueAsString(customerCartDTO);

        // Act & Assert
        MvcResult result = mockMvc.perform(post(BASE_API_PATH + "/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        assertNotNull(response, "Response should not be null");
        assertTrue(response.contains("added to the cart") || response.contains("successfully"),
                "Response should contain success message");
    }

    /**
     * Test Case 2: Add product to cart with invalid email format
     * Should return HTTP 400 Bad Request
     */
    @Test
    @DisplayName("Test 2: Add Product to Cart - Invalid Email Format")
    public void testAddProductToCart_InvalidEmailFormat() throws Exception {
        // Arrange
        customerCartDTO.setCustomerEmailId(INVALID_EMAIL_FORMAT);
        String jsonRequest = objectMapper.writeValueAsString(customerCartDTO);

        // Act & Assert
        mockMvc.perform(post(BASE_API_PATH + "/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test Case 3: Add product to cart with null email
     * Should return HTTP 400 Bad Request
     */
    @Test
    @DisplayName("Test 3: Add Product to Cart - Null Email")
    public void testAddProductToCart_NullEmail() throws Exception {
        // Arrange
        customerCartDTO.setCustomerEmailId(null);
        String jsonRequest = objectMapper.writeValueAsString(customerCartDTO);

        // Act & Assert
        mockMvc.perform(post(BASE_API_PATH + "/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test Case 4: Add product to cart with empty cart products
     * Should handle gracefully
     */
    @Test
    @DisplayName("Test 4: Add Product to Cart - Empty Cart Products")
    public void testAddProductToCart_EmptyCartProducts() throws Exception {
        // Arrange
        customerCartDTO.setCartProducts(new HashSet<>());
        String jsonRequest = objectMapper.writeValueAsString(customerCartDTO);

        // Act & Assert
        MvcResult result = mockMvc.perform(post(BASE_API_PATH + "/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();

        assertNotNull(result.getResponse().getContentAsString());
    }

    /**
     * Test Case 5: Add product with multiple items in cart
     */
    @Test
    @DisplayName("Test 5: Add Product to Cart - Multiple Products")
    public void testAddProductToCart_MultipleProducts() throws Exception {
        // Arrange
        ProductDTO productDTO2 = new ProductDTO();
        productDTO2.setProductId(2);
        productDTO2.setName("Second Product");
        productDTO2.setPrice(50.0);

        CartProductDTO cartProductDTO2 = new CartProductDTO();
        cartProductDTO2.setProduct(productDTO2);
        cartProductDTO2.setQuantity(3);

        Set<CartProductDTO> cartProducts = customerCartDTO.getCartProducts();
        cartProducts.add(cartProductDTO2);
        customerCartDTO.setCartProducts(cartProducts);

        String jsonRequest = objectMapper.writeValueAsString(customerCartDTO);

        // Act & Assert
        mockMvc.perform(post(BASE_API_PATH + "/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated());
    }

    // ======================== GET - Retrieve Products from Cart Tests ========================

    /**
     * Test Case 6: Successfully retrieve products from cart
     * Endpoint: GET /customercart-api/customer/{customerEmailId}/products
     * Expected: HTTP 200 OK with cart products
     */
    @Test
    @DisplayName("Test 6: Get Products from Cart - Success")
    public void testGetProductsFromCart_Success() throws Exception {
        // Arrange - First add a product
        String jsonRequest = objectMapper.writeValueAsString(customerCartDTO);
        mockMvc.perform(post(BASE_API_PATH + "/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated());

        // Act & Assert
        mockMvc.perform(get(BASE_API_PATH + "/customer/{customerEmailId}/products", VALID_EMAIL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    /**
     * Test Case 7: Get products from cart with invalid email format
     * Should return HTTP 400 Bad Request
     */
    @Test
    @DisplayName("Test 7: Get Products from Cart - Invalid Email Format")
    public void testGetProductsFromCart_InvalidEmailFormat() throws Exception {
        // Act & Assert
        mockMvc.perform(get(BASE_API_PATH + "/customer/{customerEmailId}/products", INVALID_EMAIL_FORMAT)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test Case 8: Get products from non-existent cart
     * Should return HTTP 400 or appropriate error
     */
    @Test
    @DisplayName("Test 8: Get Products from Cart - Cart Not Found")
    public void testGetProductsFromCart_CartNotFound() throws Exception {
        // Act & Assert - Use a valid email format but non-existent cart
        mockMvc.perform(get(BASE_API_PATH + "/customer/{customerEmailId}/products", "nonexistent@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test Case 9: Get products with special characters in email
     */
    @Test
    @DisplayName("Test 9: Get Products from Cart - Email with Special Characters")
    public void testGetProductsFromCart_EmailWithSpecialChars() throws Exception {
        // Arrange
        String specialEmail = "test.user+tag@example.com";

        // Act & Assert
        mockMvc.perform(get(BASE_API_PATH + "/customer/{customerEmailId}/products", specialEmail)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); // Should fail as cart doesn't exist
    }

    // ======================== DELETE - Delete Product from Cart Tests ========================

    /**
     * Test Case 10: Successfully delete a product from cart
     * Endpoint: DELETE /customercart-api/customer/{customerEmailId}/product/{productId}
     * Expected: HTTP 200 OK with success message
     */
    @Test
    @DisplayName("Test 10: Delete Product from Cart - Success")
    public void testDeleteProductFromCart_Success() throws Exception {
        // Arrange - First add a product
        String jsonRequest = objectMapper.writeValueAsString(customerCartDTO);
        mockMvc.perform(post(BASE_API_PATH + "/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated());

        // Act & Assert
        mockMvc.perform(delete(BASE_API_PATH + "/customer/{customerEmailId}/product/{productId}",
                VALID_EMAIL, 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"));
    }

    /**
     * Test Case 11: Delete product from cart with invalid email format
     * Should return HTTP 400 Bad Request
     */
    @Test
    @DisplayName("Test 11: Delete Product from Cart - Invalid Email Format")
    public void testDeleteProductFromCart_InvalidEmailFormat() throws Exception {
        // Act & Assert
        mockMvc.perform(delete(BASE_API_PATH + "/customer/{customerEmailId}/product/{productId}",
                INVALID_EMAIL_FORMAT, 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test Case 12: Delete product from non-existent cart
     * Should return HTTP 400 or appropriate error
     */
    @Test
    @DisplayName("Test 12: Delete Product from Cart - Cart Not Found")
    public void testDeleteProductFromCart_CartNotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(delete(BASE_API_PATH + "/customer/{customerEmailId}/product/{productId}",
                "nonexistent@example.com", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test Case 13: Delete non-existent product from cart
     * Should return HTTP 400 or appropriate error
     */
    @Test
    @DisplayName("Test 13: Delete Product from Cart - Product Not Found")
    public void testDeleteProductFromCart_ProductNotFound() throws Exception {
        // Arrange - First add a product
        String jsonRequest = objectMapper.writeValueAsString(customerCartDTO);
        mockMvc.perform(post(BASE_API_PATH + "/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated());

        // Act & Assert - Try to delete non-existent product ID
        mockMvc.perform(delete(BASE_API_PATH + "/customer/{customerEmailId}/product/{productId}",
                VALID_EMAIL, 999)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test Case 14: Delete with negative product ID
     */
    @Test
    @DisplayName("Test 14: Delete Product from Cart - Negative Product ID")
    public void testDeleteProductFromCart_NegativeProductId() throws Exception {
        // Act & Assert
        mockMvc.perform(delete(BASE_API_PATH + "/customer/{customerEmailId}/product/{productId}",
                VALID_EMAIL, -1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // ======================== PUT - Modify Product Quantity Tests ========================

    /**
     * Test Case 15: Successfully modify product quantity in cart
     * Endpoint: PUT /customercart-api/customer/{customerEmailId}/product/{productId}
     * Expected: HTTP 200 OK with success message
     */
    @Test
    @DisplayName("Test 15: Modify Product Quantity - Success")
    public void testModifyQuantityOfProductInCart_Success() throws Exception {
        // Arrange - First add a product
        String jsonRequest = objectMapper.writeValueAsString(customerCartDTO);
        mockMvc.perform(post(BASE_API_PATH + "/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated());

        // Act & Assert
        mockMvc.perform(put(BASE_API_PATH + "/customer/{customerEmailId}/product/{productId}",
                VALID_EMAIL, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"));
    }

    /**
     * Test Case 16: Modify quantity with invalid email format
     * Should return HTTP 400 Bad Request
     */
    @Test
    @DisplayName("Test 16: Modify Product Quantity - Invalid Email Format")
    public void testModifyQuantityOfProductInCart_InvalidEmailFormat() throws Exception {
        // Act & Assert
        mockMvc.perform(put(BASE_API_PATH + "/customer/{customerEmailId}/product/{productId}",
                INVALID_EMAIL_FORMAT, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("5"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test Case 17: Modify quantity for non-existent cart
     * Should return HTTP 400 or appropriate error
     */
    @Test
    @DisplayName("Test 17: Modify Product Quantity - Cart Not Found")
    public void testModifyQuantityOfProductInCart_CartNotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(put(BASE_API_PATH + "/customer/{customerEmailId}/product/{productId}",
                "nonexistent@example.com", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("5"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test Case 18: Modify quantity for non-existent product
     * Should return HTTP 400 or appropriate error
     */
    @Test
    @DisplayName("Test 18: Modify Product Quantity - Product Not Found")
    public void testModifyQuantityOfProductInCart_ProductNotFound() throws Exception {
        // Arrange - First add a product
        String jsonRequest = objectMapper.writeValueAsString(customerCartDTO);
        mockMvc.perform(post(BASE_API_PATH + "/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated());

        // Act & Assert - Try to modify non-existent product
        mockMvc.perform(put(BASE_API_PATH + "/customer/{customerEmailId}/product/{productId}",
                VALID_EMAIL, 999)
                .contentType(MediaType.APPLICATION_JSON)
                .content("5"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test Case 19: Modify quantity to zero
     */
    @Test
    @DisplayName("Test 19: Modify Product Quantity - Zero Quantity")
    public void testModifyQuantityOfProductInCart_ZeroQuantity() throws Exception {
        // Arrange - First add a product
        String jsonRequest = objectMapper.writeValueAsString(customerCartDTO);
        mockMvc.perform(post(BASE_API_PATH + "/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated());

        // Act & Assert
        mockMvc.perform(put(BASE_API_PATH + "/customer/{customerEmailId}/product/{productId}",
                VALID_EMAIL, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("0"))
                .andExpect(status().isOk());
    }

    /**
     * Test Case 20: Modify quantity to large number
     */
    @Test
    @DisplayName("Test 20: Modify Product Quantity - Large Quantity")
    public void testModifyQuantityOfProductInCart_LargeQuantity() throws Exception {
        // Arrange - First add a product
        String jsonRequest = objectMapper.writeValueAsString(customerCartDTO);
        mockMvc.perform(post(BASE_API_PATH + "/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated());

        // Act & Assert
        mockMvc.perform(put(BASE_API_PATH + "/customer/{customerEmailId}/product/{productId}",
                VALID_EMAIL, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("1000"))
                .andExpect(status().isOk());
    }

    /**
     * Test Case 21: Modify quantity with negative value
     */
    @Test
    @DisplayName("Test 21: Modify Product Quantity - Negative Quantity")
    public void testModifyQuantityOfProductInCart_NegativeQuantity() throws Exception {
        // Arrange - First add a product
        String jsonRequest = objectMapper.writeValueAsString(customerCartDTO);
        mockMvc.perform(post(BASE_API_PATH + "/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated());

        // Act & Assert
        mockMvc.perform(put(BASE_API_PATH + "/customer/{customerEmailId}/product/{productId}",
                VALID_EMAIL, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("-5"))
                .andExpect(status().isOk());
    }

    // ======================== DELETE - Delete All Products from Cart Tests ========================

    /**
     * Test Case 22: Successfully delete all products from cart
     * Endpoint: DELETE /customercart-api/customer/{customerEmailId}/products
     * Expected: HTTP 200 OK with success message
     */
    @Test
    @DisplayName("Test 22: Delete All Products from Cart - Success")
    public void testDeleteAllProductsFromCart_Success() throws Exception {
        // Arrange - First add a product
        String jsonRequest = objectMapper.writeValueAsString(customerCartDTO);
        mockMvc.perform(post(BASE_API_PATH + "/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated());

        // Act & Assert
        mockMvc.perform(delete(BASE_API_PATH + "/customer/{customerEmailId}/products", VALID_EMAIL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"));
    }

    /**
     * Test Case 23: Delete all products with invalid email format
     * Should return HTTP 400 Bad Request
     */
    @Test
    @DisplayName("Test 23: Delete All Products from Cart - Invalid Email Format")
    public void testDeleteAllProductsFromCart_InvalidEmailFormat() throws Exception {
        // Act & Assert
        mockMvc.perform(delete(BASE_API_PATH + "/customer/{customerEmailId}/products", INVALID_EMAIL_FORMAT)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test Case 24: Delete all products from non-existent cart
     * Should return HTTP 400 or appropriate error
     */
    @Test
    @DisplayName("Test 24: Delete All Products from Cart - Cart Not Found")
    public void testDeleteAllProductsFromCart_CartNotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(delete(BASE_API_PATH + "/customer/{customerEmailId}/products", "nonexistent@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test Case 25: Delete all products from empty cart
     * Should return appropriate response
     */
    @Test
    @DisplayName("Test 25: Delete All Products from Cart - Empty Cart")
    public void testDeleteAllProductsFromCart_EmptyCart() throws Exception {
        // Arrange - Add cart without products
        customerCartDTO.setCartProducts(new HashSet<>());
        String jsonRequest = objectMapper.writeValueAsString(customerCartDTO);
        mockMvc.perform(post(BASE_API_PATH + "/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated());

        // Act & Assert
        mockMvc.perform(delete(BASE_API_PATH + "/customer/{customerEmailId}/products", VALID_EMAIL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); // Should fail - no products to delete
    }

    // ======================== Content Type and Request Format Tests ========================

    /**
     * Test Case 26: Add product with invalid JSON format
     * Should return HTTP 400 Bad Request
     */
    @Test
    @DisplayName("Test 26: Add Product to Cart - Invalid JSON Format")
    public void testAddProductToCart_InvalidJsonFormat() throws Exception {
        // Arrange
        String invalidJson = "{invalid json}";

        // Act & Assert
        mockMvc.perform(post(BASE_API_PATH + "/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test Case 27: Add product with missing content type
     */
    @Test
    @DisplayName("Test 27: Add Product to Cart - Missing Content Type")
    public void testAddProductToCart_MissingContentType() throws Exception {
        // Arrange
        String jsonRequest = objectMapper.writeValueAsString(customerCartDTO);

        // Act & Assert
        mockMvc.perform(post(BASE_API_PATH + "/products")
                .content(jsonRequest))
                .andExpect(status().isUnsupportedMediaType());
    }

    // ======================== Email Validation Tests ========================

    /**
     * Test Case 28: Valid email formats
     */
    @Test
    @DisplayName("Test 28: Valid Email Formats - Multiple Variants")
    public void testValidEmailFormats() throws Exception {
        String[] validEmails = {
                "simple@example.com",
                "very.common@example.com",
                "user+tag@example.co.uk",
                "user_123@example.com"
        };

        for (String email : validEmails) {
            customerCartDTO.setCustomerEmailId(email);
            String jsonRequest = objectMapper.writeValueAsString(customerCartDTO);

            mockMvc.perform(post(BASE_API_PATH + "/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
                    .andExpect(status().isCreated());
        }
    }

    /**
     * Test Case 29: Invalid email formats
     */
    @Test
    @DisplayName("Test 29: Invalid Email Formats - Multiple Variants")
    public void testInvalidEmailFormats() throws Exception {
        String[] invalidEmails = {
                "plainaddress",
                "@nodomain.com",
                "missing@domain",
                "user@.com",
                "user name@example.com"
        };

        for (String email : invalidEmails) {
            customerCartDTO.setCustomerEmailId(email);
            String jsonRequest = objectMapper.writeValueAsString(customerCartDTO);

            mockMvc.perform(post(BASE_API_PATH + "/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
                    .andExpect(status().isBadRequest());
        }
    }

    // ======================== End-to-End Workflow Tests ========================

    /**
     * Test Case 30: Complete workflow - Add, Retrieve, Modify, Delete
     */
    @Test
    @DisplayName("Test 30: Complete Workflow - Add, Get, Modify, Delete")
    public void testCompleteWorkflow() throws Exception {
        // Step 1: Add product to cart
        String jsonRequest = objectMapper.writeValueAsString(customerCartDTO);
        MvcResult addResult = mockMvc.perform(post(BASE_API_PATH + "/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();
        assertNotNull(addResult.getResponse().getContentAsString());

        // Step 2: Get products from cart
        mockMvc.perform(get(BASE_API_PATH + "/customer/{customerEmailId}/products", VALID_EMAIL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Step 3: Modify product quantity
        mockMvc.perform(put(BASE_API_PATH + "/customer/{customerEmailId}/product/{productId}",
                VALID_EMAIL, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("5"))
                .andExpect(status().isOk());

        // Step 4: Delete single product
        mockMvc.perform(delete(BASE_API_PATH + "/customer/{customerEmailId}/product/{productId}",
                VALID_EMAIL, 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test Case 31: Multiple operations for different products
     */
    @Test
    @DisplayName("Test 31: Multiple Operations - Different Products")
    public void testMultipleOperations() throws Exception {
        // Add first product
        String jsonRequest1 = objectMapper.writeValueAsString(customerCartDTO);
        mockMvc.perform(post(BASE_API_PATH + "/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest1))
                .andExpect(status().isCreated());

        // Add second product
        ProductDTO productDTO2 = new ProductDTO();
        productDTO2.setProductId(2);
        productDTO2.setName("Second Product");
        productDTO2.setPrice(75.0);

        CartProductDTO cartProductDTO2 = new CartProductDTO();
        cartProductDTO2.setProduct(productDTO2);
        cartProductDTO2.setQuantity(1);

        CustomerCartDTO customerCartDTO2 = new CustomerCartDTO();
        customerCartDTO2.setCustomerEmailId(VALID_EMAIL);
        Set<CartProductDTO> cartProducts2 = new HashSet<>();
        cartProducts2.add(cartProductDTO2);
        customerCartDTO2.setCartProducts(cartProducts2);

        String jsonRequest2 = objectMapper.writeValueAsString(customerCartDTO2);
        mockMvc.perform(post(BASE_API_PATH + "/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest2))
                .andExpect(status().isCreated());

        // Modify quantity of second product
        mockMvc.perform(put(BASE_API_PATH + "/customer/{customerEmailId}/product/{productId}",
                VALID_EMAIL, 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content("3"))
                .andExpect(status().isOk());
    }

}


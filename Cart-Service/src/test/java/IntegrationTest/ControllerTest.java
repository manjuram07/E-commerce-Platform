//package IntegrationTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.infy.ekart.cart.dto.CartProductDTO;
//import com.infy.ekart.cart.dto.CustomerCartDTO;
//import com.infy.ekart.cart.dto.ProductDTO;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@TestPropertySource(locations = "classpath:application.properties")
//public class ControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private CustomerCartDTO customerCartDTO;
//    private CartProductDTO cartProductDTO;
//    private ProductDTO productDTO;
//
//    @BeforeEach
//    public void setUp() {
//        // Initialize test data
//        productDTO = new ProductDTO();
//        productDTO.setProductId(101);
////        productDTO.setProductName("Laptop");
////        productDTO.setProductDescription("High-performance laptop");
////        productDTO.setProductPrice(50000.0);
//
//        cartProductDTO = new CartProductDTO();
//        cartProductDTO.setProduct(productDTO);
//        cartProductDTO.setQuantity(2);
//
//        customerCartDTO = new CustomerCartDTO();
//        customerCartDTO.setCustomerEmailId("customer@example.com");
//        Set<CartProductDTO> cartProducts = new HashSet<>();
//        cartProducts.add(cartProductDTO);
//        customerCartDTO.setCartProducts(cartProducts);
//    }
//
//    /**
//     * Test Case 1: Add a product to the cart successfully
//     * POST /customercart-api/products
//     */
//    @Test
//    public void testAddProductToCart_Success() throws Exception {
//        // Arrange
//        String jsonRequest = objectMapper.writeValueAsString(customerCartDTO);
//
//        // Act & Assert
//        MvcResult result = mockMvc.perform(post("/Ekart/customercart-api/products")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(jsonRequest))
//                .andExpect(status().isCreated())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andReturn();
//
//        String response = result.getResponse().getContentAsString();
//        assertTrue(response.contains("successfully added to the cart"),
//            "Response should contain success message");
//    }
//
//    /**
//     * Test Case 2: Add product to cart with invalid email format
//     * Should return 400 Bad Request
//     */
//    @Test
//    public void testAddProductToCart_InvalidEmailFormat() throws Exception {
//        // Arrange
//        customerCartDTO.setCustomerEmailId("invalidemail");
//        String jsonRequest = objectMapper.writeValueAsString(customerCartDTO);
//
//        // Act & Assert
//        mockMvc.perform(post("/Ekart/customercart-api/products")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(jsonRequest))
//                .andExpect(status().isBadRequest());
//    }
//
//    /**
//     * Test Case 3: Add product to cart with null email
//     * Should return 400 Bad Request
//     */
//    @Test
//    public void testAddProductToCart_NullEmail() throws Exception {
//        // Arrange
//        customerCartDTO.setCustomerEmailId(null);
//        String jsonRequest = objectMapper.writeValueAsString(customerCartDTO);
//
//        // Act & Assert
//        mockMvc.perform(post("/Ekart/customercart-api/products")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(jsonRequest))
//                .andExpect(status().isBadRequest());
//    }
//
//    /**
//     * Test Case 4: Get products from cart successfully
//     * GET /customercart-api/customer/{customerEmailId}/products
//     */
//    @Test
//    public void testGetProductsFromCart_Success() throws Exception {
//        // Arrange
//        String customerEmail = "customer@example.com";
//        // First add a product to cart
//        Set<CartProductDTO> cartProducts = new HashSet<>();
//        cartProducts.add(cartProductDTO);
//        customerCartDTO.setCartProducts(cartProducts);
//        String jsonRequest = objectMapper.writeValueAsString(customerCartDTO);
//
//        mockMvc.perform(post("/Ekart/customercart-api/products")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(jsonRequest))
//                .andExpect(status().isCreated());
//
//        // Act & Assert
//        mockMvc.perform(get("/Ekart/customercart-api/customer/{customerEmailId}/products", customerEmail)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
//    }
//
//    /**
//     * Test Case 5: Get products from cart with invalid email format
//     * Should return 400 Bad Request
//     */
//    @Test
//    public void testGetProductsFromCart_InvalidEmailFormat() throws Exception {
//        // Arrange
//        String invalidEmail = "invalidemail";
//
//        // Act & Assert
//        mockMvc.perform(get("/Ekart/customercart-api/customer/{customerEmailId}/products", invalidEmail)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    /**
//     * Test Case 6: Modify quantity of product in cart successfully
//     * PUT /customercart-api/customer/{customerEmailId}/product/{productId}
//     */
//    @Test
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    public void testModifyQuantityOfProductInCart_Success() throws Exception {
//        // Arrange
//        String customerEmail = "customer@example.com";
//        Integer productId = 101;
//        Integer newQuantity = 5;
//
//        // First add a product to cart
//        String jsonRequest = objectMapper.writeValueAsString(customerCartDTO);
//        mockMvc.perform(post("/Ekart/customercart-api/products")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(jsonRequest))
//                .andExpect(status().isCreated());
//
//        // Act & Assert
//        mockMvc.perform(put("/Ekart/customercart-api/customer/{customerEmailId}/product/{productId}",
//                    customerEmail, productId)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(String.valueOf(newQuantity)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Quantity updated successfully"));
//    }
//
//    /**
//     * Test Case 7: Modify quantity with invalid email format
//     * Should return 400 Bad Request
//     */
//    @Test
//    public void testModifyQuantityOfProductInCart_InvalidEmailFormat() throws Exception {
//        // Arrange
//        String invalidEmail = "invalidemail";
//        Integer productId = 101;
//        Integer newQuantity = 5;
//
//        // Act & Assert
//        mockMvc.perform(put("/Ekart/customercart-api/customer/{customerEmailId}/product/{productId}",
//                    invalidEmail, productId)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(String.valueOf(newQuantity)))
//                .andExpect(status().isBadRequest());
//    }
//
//    /**
//     * Test Case 8: Delete a product from cart successfully
//     * DELETE /customercart-api/customer/{customerEmailId}/product/{productId}
//     */
//    @Test
//    public void testDeleteProductFromCart_Success() throws Exception {
//        // Arrange
//        String customerEmail = "customer@example.com";
//        Integer productId = 101;
//
//        // First add a product to cart
//        String jsonRequest = objectMapper.writeValueAsString(customerCartDTO);
//        mockMvc.perform(post("/Ekart/customercart-api/products")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(jsonRequest))
//                .andExpect(status().isCreated());
//
//        // Act & Assert
//        mockMvc.perform(delete("/Ekart/customercart-api/customer/{customerEmailId}/product/{productId}",
//                    customerEmail, productId)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Your item has been removed from cart."));
//    }
//
//    /**
//     * Test Case 9: Delete product from cart with invalid email format
//     * Should return 400 Bad Request
//     */
//    @Test
//    public void testDeleteProductFromCart_InvalidEmailFormat() throws Exception {
//        // Arrange
//        String invalidEmail = "invalidemail";
//        Integer productId = 101;
//
//        // Act & Assert
//        mockMvc.perform(delete("/Ekart/customercart-api/customer/{customerEmailId}/product/{productId}",
//                    invalidEmail, productId)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    /**
//     * Test Case 10: Delete all products from cart successfully
//     * DELETE /customercart-api/customer/{customerEmailId}/products
//     */
//    @Test
//    public void testDeleteAllProductsFromCart_Success() throws Exception {
//        // Arrange
//        String customerEmail = "customer@example.com";
//
//        // First add a product to cart
//        String jsonRequest = objectMapper.writeValueAsString(customerCartDTO);
//        mockMvc.perform(post("/Ekart/customercart-api/products")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(jsonRequest))
//                .andExpect(status().isCreated());
//
//        // Act & Assert
//        mockMvc.perform(delete("/Ekart/customercart-api/customer/{customerEmailId}/products", customerEmail)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().string("All products deleted from your cart."));
//    }
//
//    /**
//     * Test Case 11: Delete all products from cart with invalid email format
//     * Should return 400 Bad Request
//     */
//    @Test
//    public void testDeleteAllProductsFromCart_InvalidEmailFormat() throws Exception {
//        // Arrange
//        String invalidEmail = "invalidemail";
//
//        // Act & Assert
//        mockMvc.perform(delete("/Ekart/customercart-api/customer/{customerEmailId}/products", invalidEmail)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    /**
//     * Test Case 12: Get products from non-existent cart
//     * Should handle the case gracefully
//     */
//    @Test
//    public void testGetProductsFromCart_NonExistentCart() throws Exception {
//        // Arrange
//        String customerEmail = "nonexistent@example.com";
//
//        // Act & Assert - This might return empty set or 404 depending on implementation
//        mockMvc.perform(get("/Ekart/customercart-api/customer/{customerEmailId}/products", customerEmail)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }
//
//    /**
//     * Test Case 13: Add product with negative quantity (validation failure)
//     * Should return 400 Bad Request
//     */
//    @Test
//    public void testAddProductToCart_NegativeQuantity() throws Exception {
//        // Arrange
//        cartProductDTO.setQuantity(-1);
//        customerCartDTO.setCartProducts(Set.of(cartProductDTO));
//        String jsonRequest = objectMapper.writeValueAsString(customerCartDTO);
//
//        // Act & Assert
//        mockMvc.perform(post("/Ekart/customercart-api/products")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(jsonRequest))
//                .andExpect(status().isBadRequest());
//    }
//
//    /**
//     * Test Case 14: Add product to cart with valid email format variations
//     * Should accept different valid email formats
//     */
//    @Test
//    public void testAddProductToCart_ValidEmailFormatVariations() throws Exception {
//        // Arrange
//        String[] validEmails = {
//            "user@example.com",
//            "user.name@example.co.uk",
//            "user_123@test.org",
//            "a@b.co"
//        };
//
//        for (String email : validEmails) {
//            customerCartDTO.setCustomerEmailId(email);
//            String jsonRequest = objectMapper.writeValueAsString(customerCartDTO);
//
//            // Act & Assert
//            mockMvc.perform(post("/Ekart/customercart-api/products")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(jsonRequest))
//                    .andExpect(status().isCreated());
//        }
//    }
//}

package UnitTesting;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import com.infy.ekart.cart.dto.CartProductDTO;
import com.infy.ekart.cart.dto.CustomerCartDTO;
import com.infy.ekart.cart.dto.ProductDTO;
import com.infy.ekart.cart.entity.CartProduct;
import com.infy.ekart.cart.entity.CustomerCart;
import com.infy.ekart.cart.exception.EKartCustomerCartException;
import com.infy.ekart.cart.repository.CartProductRepository;
import com.infy.ekart.cart.repository.CustomerCartRepository;
import com.infy.ekart.cart.service.CustomerCartServiceImpl;
import org.springframework.web.reactive.function.client.WebClient;


@ExtendWith(MockitoExtension.class)
public class CustomerCartTest {

    @Mock
    private CustomerCartRepository customerCartRepository;

    @Mock
    private CartProductRepository cartProductRepository;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private CustomerCartServiceImpl customerCartService;

    private CustomerCartDTO customerCartDTO;
    private CustomerCart customerCart;
    private CartProduct cartProduct;
    private CartProductDTO cartProductDTO;
    private ProductDTO productDTO;

    @BeforeEach
    public void setUp() {
        // Initialize ProductDTO
        productDTO = new ProductDTO();
        productDTO.setProductId(1);
        productDTO.setName("Test Product");
        productDTO.setDescription("Test Description");
        productDTO.setCategory("Test Category");
        productDTO.setBrand("Test Brand");
        productDTO.setPrice(100.0);
        productDTO.setAvailableQuantity(10);

        // Initialize CartProductDTO
        cartProductDTO = new CartProductDTO();
        cartProductDTO.setCartProductId(1);
        cartProductDTO.setProduct(productDTO);
        cartProductDTO.setQuantity(2);

        // Initialize Set of CartProductDTOs
        Set<CartProductDTO> cartProductDTOs = new HashSet<>();
        cartProductDTOs.add(cartProductDTO);

        // Initialize CustomerCartDTO
        customerCartDTO = new CustomerCartDTO();
        customerCartDTO.setCartId(1);
        customerCartDTO.setCustomerEmailId("test@example.com");
        customerCartDTO.setCartProducts(cartProductDTOs);

        // Initialize CartProduct
        cartProduct = new CartProduct();
        cartProduct.setCartProductId(1);
        cartProduct.setProductId(1);
        cartProduct.setQuantity(2);

        // Initialize CustomerCart
        Set<CartProduct> cartProducts = new HashSet<>();
        cartProducts.add(cartProduct);

        customerCart = new CustomerCart();
        customerCart.setCartId(1);
        customerCart.setCustomerEmailId("test@example.com");
        customerCart.setCartProducts(cartProducts);
    }

    // Test for addProductToCart - New Cart
//    @Test
//    @DisplayName("Test addProductToCart - New Cart")
//    public void testAddProductToCartNewCart() throws EKartCustomerCartException {
//        // Arrange
//        when(customerCartRepository.findByCustomerEmailId("test@example.com"))
//                .thenReturn(Optional.empty());
//        when(customerCartRepository.save(any(CustomerCart.class)))
//                .thenReturn(customerCart);
//
//        // Act
//        Integer cartId = customerCartService.addProductToCart(customerCartDTO);
//
//        // Assert
//        assertNotNull(cartId);
//        assertEquals(1, cartId);
//        verify(customerCartRepository, times(1)).findByCustomerEmailId("test@example.com");
//        verify(customerCartRepository, times(1)).save(any(CustomerCart.class));
//    }

    // Test for addProductToCart - Existing Cart with New Product
    @Test
    @DisplayName("Test addProductToCart - Existing Cart with New Product")
    public void testAddProductToCartExistingCartNewProduct() throws EKartCustomerCartException {
        // Arrange
        when(customerCartRepository.findByCustomerEmailId("test@example.com"))
                .thenReturn(Optional.of(customerCart));

        // Create a new product to add
        ProductDTO newProductDTO = new ProductDTO();
        newProductDTO.setProductId(2);
        CartProductDTO newCartProductDTO = new CartProductDTO();
        newCartProductDTO.setProduct(newProductDTO);
        newCartProductDTO.setQuantity(5);

        Set<CartProductDTO> newCartProducts = new HashSet<>();
        newCartProducts.add(newCartProductDTO);

        CustomerCartDTO newCustomerCartDTO = new CustomerCartDTO();
        newCustomerCartDTO.setCustomerEmailId("test@example.com");
        newCustomerCartDTO.setCartProducts(newCartProducts);

        // Act
        Integer cartId = customerCartService.addProductToCart(newCustomerCartDTO);

        // Assert
        assertNotNull(cartId);
        assertEquals(1, cartId);
        verify(customerCartRepository, times(1)).findByCustomerEmailId("test@example.com");
    }

    // Test for addProductToCart - Existing Cart with Existing Product (Quantity Update)
    @Test
    @DisplayName("Test addProductToCart - Existing Cart with Existing Product")
    public void testAddProductToCartExistingCartExistingProduct() throws EKartCustomerCartException {
        // Arrange
        when(customerCartRepository.findByCustomerEmailId("test@example.com"))
                .thenReturn(Optional.of(customerCart));

        // Create a product that already exists in the cart
        ProductDTO existingProductDTO = new ProductDTO();
        existingProductDTO.setProductId(1);
        CartProductDTO existingCartProductDTO = new CartProductDTO();
        existingCartProductDTO.setProduct(existingProductDTO);
        existingCartProductDTO.setQuantity(3); // Adding 3 more to existing 2 = 5 total

        Set<CartProductDTO> existingCartProducts = new HashSet<>();
        existingCartProducts.add(existingCartProductDTO);

        CustomerCartDTO existingCustomerCartDTO = new CustomerCartDTO();
        existingCustomerCartDTO.setCustomerEmailId("test@example.com");
        existingCustomerCartDTO.setCartProducts(existingCartProducts);

        // Act
        Integer cartId = customerCartService.addProductToCart(existingCustomerCartDTO);

        // Assert
        assertNotNull(cartId);
        assertEquals(1, cartId);
        verify(customerCartRepository, times(1)).findByCustomerEmailId("test@example.com");
    }

    // Test for getProductsFromCart - Success
//    @Test
//    @DisplayName("Test getProductsFromCart - Success")
//    public void testGetProductsFromCartSuccess() throws EKartCustomerCartException {
//        // Arrange
//        when(customerCartRepository.findByCustomerEmailId("test@example.com"))
//                .thenReturn(Optional.of(customerCart));
//
//        // Mock WebClient calls
//        when(webClient.get()).thenReturn(mock(org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec.class));
//
//        // Act
//        Set<CartProductDTO> result = customerCartService.getProductsFromCart("test@example.com");
//
//        // Assert
//        assertNotNull(result);
//        verify(customerCartRepository, times(1)).findByCustomerEmailId("test@example.com");
//    }

    // Test for getProductsFromCart - Cart Not Found
    @Test
    @DisplayName("Test getProductsFromCart - Cart Not Found")
    public void testGetProductsFromCartCartNotFound() throws EKartCustomerCartException {
        // Arrange
        when(customerCartRepository.findByCustomerEmailId("test@example.com"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EKartCustomerCartException.class, () -> {
            customerCartService.getProductsFromCart("test@example.com");
        });
        verify(customerCartRepository, times(1)).findByCustomerEmailId("test@example.com");
    }

    // Test for getProductsFromCart - No Products in Cart
    @Test
    @DisplayName("Test getProductsFromCart - No Products in Cart")
    public void testGetProductsFromCartNoProductsInCart() throws EKartCustomerCartException {
        // Arrange
        CustomerCart emptyCart = new CustomerCart();
        emptyCart.setCartId(1);
        emptyCart.setCustomerEmailId("test@example.com");
        emptyCart.setCartProducts(new HashSet<>());

        when(customerCartRepository.findByCustomerEmailId("test@example.com"))
                .thenReturn(Optional.of(emptyCart));

        // Act & Assert
        assertThrows(EKartCustomerCartException.class, () -> {
            customerCartService.getProductsFromCart("test@example.com");
        });
        verify(customerCartRepository, times(1)).findByCustomerEmailId("test@example.com");
    }

    // Test for deleteProductFromCart - Success
    @Test
    @DisplayName("Test deleteProductFromCart - Success")
    public void testDeleteProductFromCartSuccess() throws EKartCustomerCartException {
        // Arrange
        when(customerCartRepository.findByCustomerEmailId("test@example.com"))
                .thenReturn(Optional.of(customerCart));

        // Act
        customerCartService.deleteProductFromCart("test@example.com", 1);

        // Assert
        verify(customerCartRepository, times(1)).findByCustomerEmailId("test@example.com");
        verify(cartProductRepository, times(1)).delete(cartProduct);
    }

    // Test for deleteProductFromCart - Cart Not Found
    @Test
    @DisplayName("Test deleteProductFromCart - Cart Not Found")
    public void testDeleteProductFromCartCartNotFound() throws EKartCustomerCartException {
        // Arrange
        when(customerCartRepository.findByCustomerEmailId("test@example.com"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EKartCustomerCartException.class, () -> {
            customerCartService.deleteProductFromCart("test@example.com", 1);
        });
        verify(customerCartRepository, times(1)).findByCustomerEmailId("test@example.com");
    }

    // Test for deleteProductFromCart - No Products in Cart
    @Test
    @DisplayName("Test deleteProductFromCart - No Products in Cart")
    public void testDeleteProductFromCartNoProductsInCart() throws EKartCustomerCartException {
        // Arrange
        CustomerCart emptyCart = new CustomerCart();
        emptyCart.setCartId(1);
        emptyCart.setCustomerEmailId("test@example.com");
        emptyCart.setCartProducts(new HashSet<>());

        when(customerCartRepository.findByCustomerEmailId("test@example.com"))
                .thenReturn(Optional.of(emptyCart));

        // Act & Assert
        assertThrows(EKartCustomerCartException.class, () -> {
            customerCartService.deleteProductFromCart("test@example.com", 1);
        });
        verify(customerCartRepository, times(1)).findByCustomerEmailId("test@example.com");
    }

    // Test for deleteProductFromCart - Product Not Found
    @Test
    @DisplayName("Test deleteProductFromCart - Product Not Found")
    public void testDeleteProductFromCartProductNotFound() throws EKartCustomerCartException {
        // Arrange
        when(customerCartRepository.findByCustomerEmailId("test@example.com"))
                .thenReturn(Optional.of(customerCart));

        // Act & Assert
        assertThrows(EKartCustomerCartException.class, () -> {
            customerCartService.deleteProductFromCart("test@example.com", 999);
        });
        verify(customerCartRepository, times(1)).findByCustomerEmailId("test@example.com");
    }

    // Test for deleteAllProductsFromCart - Success
//    @Test
//    @DisplayName("Test deleteAllProductsFromCart - Success")
//    public void testDeleteAllProductsFromCartSuccess() throws EKartCustomerCartException {
//        // Arrange
//        when(customerCartRepository.findByCustomerEmailId("test@example.com"))
//                .thenReturn(Optional.of(customerCart));
//
//        // Act
//        customerCartService.deleteAllProductsFromCart("test@example.com");
//
//        // Assert
//        verify(customerCartRepository, times(1)).findByCustomerEmailId("test@example.com");
//        verify(cartProductRepository, times(1)).deleteById(1);
//    }

    // Test for deleteAllProductsFromCart - Cart Not Found
    @Test
    @DisplayName("Test deleteAllProductsFromCart - Cart Not Found")
    public void testDeleteAllProductsFromCartCartNotFound() throws EKartCustomerCartException {
        // Arrange
        when(customerCartRepository.findByCustomerEmailId("test@example.com"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EKartCustomerCartException.class, () -> {
            customerCartService.deleteAllProductsFromCart("test@example.com");
        });
        verify(customerCartRepository, times(1)).findByCustomerEmailId("test@example.com");
    }

    // Test for deleteAllProductsFromCart - No Products in Cart
    @Test
    @DisplayName("Test deleteAllProductsFromCart - No Products in Cart")
    public void testDeleteAllProductsFromCartNoProductsInCart() throws EKartCustomerCartException {
        // Arrange
        CustomerCart emptyCart = new CustomerCart();
        emptyCart.setCartId(1);
        emptyCart.setCustomerEmailId("test@example.com");
        emptyCart.setCartProducts(new HashSet<>());

        when(customerCartRepository.findByCustomerEmailId("test@example.com"))
                .thenReturn(Optional.of(emptyCart));

        // Act & Assert
        assertThrows(EKartCustomerCartException.class, () -> {
            customerCartService.deleteAllProductsFromCart("test@example.com");
        });
        verify(customerCartRepository, times(1)).findByCustomerEmailId("test@example.com");
    }

    // Test for modifyQuantityOfProductInCart - Success
    @Test
    @DisplayName("Test modifyQuantityOfProductInCart - Success")
    public void testModifyQuantityOfProductInCartSuccess() throws EKartCustomerCartException {
        // Arrange
        when(customerCartRepository.findByCustomerEmailId("test@example.com"))
                .thenReturn(Optional.of(customerCart));

        // Act
        customerCartService.modifyQuantityOfProductInCart("test@example.com", 1, 5);

        // Assert
        assertEquals(5, cartProduct.getQuantity());
        verify(customerCartRepository, times(1)).findByCustomerEmailId("test@example.com");
    }

    // Test for modifyQuantityOfProductInCart - Cart Not Found
    @Test
    @DisplayName("Test modifyQuantityOfProductInCart - Cart Not Found")
    public void testModifyQuantityOfProductInCartCartNotFound() throws EKartCustomerCartException {
        // Arrange
        when(customerCartRepository.findByCustomerEmailId("test@example.com"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EKartCustomerCartException.class, () -> {
            customerCartService.modifyQuantityOfProductInCart("test@example.com", 1, 5);
        });
        verify(customerCartRepository, times(1)).findByCustomerEmailId("test@example.com");
    }

    // Test for modifyQuantityOfProductInCart - No Products in Cart
    @Test
    @DisplayName("Test modifyQuantityOfProductInCart - No Products in Cart")
    public void testModifyQuantityOfProductInCartNoProductsInCart() throws EKartCustomerCartException {
        // Arrange
        CustomerCart emptyCart = new CustomerCart();
        emptyCart.setCartId(1);
        emptyCart.setCustomerEmailId("test@example.com");
        emptyCart.setCartProducts(new HashSet<>());

        when(customerCartRepository.findByCustomerEmailId("test@example.com"))
                .thenReturn(Optional.of(emptyCart));

        // Act & Assert
        assertThrows(EKartCustomerCartException.class, () -> {
            customerCartService.modifyQuantityOfProductInCart("test@example.com", 1, 5);
        });
        verify(customerCartRepository, times(1)).findByCustomerEmailId("test@example.com");
    }

    // Test for modifyQuantityOfProductInCart - Product Not Found
    @Test
    @DisplayName("Test modifyQuantityOfProductInCart - Product Not Found")
    public void testModifyQuantityOfProductInCartProductNotFound() throws EKartCustomerCartException {
        // Arrange
        when(customerCartRepository.findByCustomerEmailId("test@example.com"))
                .thenReturn(Optional.of(customerCart));

        // Act & Assert
        assertThrows(EKartCustomerCartException.class, () -> {
            customerCartService.modifyQuantityOfProductInCart("test@example.com", 999, 5);
        });
        verify(customerCartRepository, times(1)).findByCustomerEmailId("test@example.com");
    }

    // Test for modifyQuantityOfProductInCart - Update with Zero Quantity
    @Test
    @DisplayName("Test modifyQuantityOfProductInCart - Update with Zero Quantity")
    public void testModifyQuantityOfProductInCartZeroQuantity() throws EKartCustomerCartException {
        // Arrange
        when(customerCartRepository.findByCustomerEmailId("test@example.com"))
                .thenReturn(Optional.of(customerCart));

        // Act
        customerCartService.modifyQuantityOfProductInCart("test@example.com", 1, 0);

        // Assert
        assertEquals(0, cartProduct.getQuantity());
        verify(customerCartRepository, times(1)).findByCustomerEmailId("test@example.com");
    }

}

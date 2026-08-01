package com.infy.ekart.product;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@OpenAPIDefinition(
        info = @Info(
                title = "Product Service API",
                version = "v1.0",
                description = "API for managing products in the banking system",
                contact = @Contact(
                        name = "Manjunath K H",
                        email = "khmanjunatha405@gmail.com"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Product service Documentation",
                url = "https://example.com/docs/product-service"
        )
)
@SpringBootApplication
//@PropertySource(value = { "classpath:messages.properties" })
//@EnableDiscoveryClient
public class ProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductApplication.class, args);
	}

}

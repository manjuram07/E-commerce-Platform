package com.infy.ekart.customer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
	@NotNull(message = "{cartproduct.productid.absent}")
	private Integer productId;
	private String name;
	private String description;
	private String category;
	private String brand;
	private Double price;
	private Integer availableQuantity;


}

package com.infy.ekart.product.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductDTO {
	
	private Integer productId;
	private String name;
	private String description;
	private String category;
	private String brand;
	private Double price;
	private Integer availableQuantity;


}

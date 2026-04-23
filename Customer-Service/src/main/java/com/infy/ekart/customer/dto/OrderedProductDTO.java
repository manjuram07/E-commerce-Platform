package com.infy.ekart.customer.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderedProductDTO {

	
	private Integer orderedProductId;
	private ProductDTO product;
	private Integer quantity;


}

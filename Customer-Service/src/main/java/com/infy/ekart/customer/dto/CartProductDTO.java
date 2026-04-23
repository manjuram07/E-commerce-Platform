package com.infy.ekart.customer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartProductDTO {
	private Integer cartProductId;
	
	@Valid
	private ProductDTO product;
	@PositiveOrZero(message = "{cartproduct.invalid.quantity}")
	private Integer quantity;


    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((product.getProductId() == null) ? 0 : product.getProductId().hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CartProductDTO other = (CartProductDTO) obj;
		if (product.getProductId() == null) {
			if (other.product.getProductId() != null)
				return false;
		} else if (!product.getProductId().equals(other.product.getProductId()))
			return false;
		return true;
	}


}

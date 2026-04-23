package com.infy.ekart.customer.dto;

import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCartDTO {
	
	private Integer cartId;
	@NotNull(message = "{customeremail.absent}")
	@Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+" , message = "{invalid.customeremail.format}")
	private String customerEmailId;
	@Valid
	private Set<CartProductDTO> cartProducts;


}

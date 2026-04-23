package com.infy.ekart.customer.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
	
	@NotNull(message = "{email.absent}")
	@Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}")
	private String emailId;
	@Pattern(regexp = "([A-Za-z])+(\\s[A-Za-z]+)*", message = "{customer.invalid.name}")
	private String name;
	@NotNull(message = "{password.absent}")
	@Pattern(regexp = ".*[A-Z]+.*", message = "{invalid.password.format.uppercase}")
	@Pattern(regexp = ".*[a-z]+.*", message = "{invalid.password.format.lowercase}")
	@Pattern(regexp = ".*[0-9]+.*", message = "{invalid.password.format.number}")
	@Pattern(regexp = ".*[^a-zA-Z-0-9].*", message = "{invalid.password.format.specialcharacter}")
	private String password;
	private String newPassword;
	@Size(max=10, min = 10, message = "{customer.invalid.phonenumber}")
	@Pattern(regexp = "[0-9]+", message = "{customer.invalid.phonenumber}")
	private String phoneNumber;
	@NotNull(message = "{customer.address.absent}")
	private String address;


}

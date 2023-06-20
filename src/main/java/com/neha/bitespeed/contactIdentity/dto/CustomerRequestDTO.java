package com.neha.bitespeed.contactIdentity.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class CustomerRequestDTO {

	@Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
	        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", 
	        message = "enter valid email format")
	private String email;
	
	@Pattern(regexp = "[1-9][0-9]{5,}", message = "phoneNumber should have 6 - 10 digits")
	private String phoneNumber;
}
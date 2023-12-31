package com.neha.bitespeed.contactIdentity.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.neha.bitespeed.contactIdentity.dto.CustomerRequestDTO;
import com.neha.bitespeed.contactIdentity.dto.CustomerResponseDTO;
import com.neha.bitespeed.contactIdentity.exception.CustomerAlreadyExistException;
import com.neha.bitespeed.contactIdentity.exception.InvalidCustomerRequestException;
import com.neha.bitespeed.contactIdentity.service.ContactIdentityService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@OpenAPIDefinition(info = @Info(title = "contact-identity-service"))
@Tag(name = "Identity Reconciliation REST Endpoints")
public class ContactIdentityController {

	@Autowired
	private ContactIdentityService identityService;
	
	@PostMapping(value = "/identify")
	public CustomerResponseDTO registerCustomer(@Valid @RequestBody CustomerRequestDTO customerRequest) throws CustomerAlreadyExistException, InvalidCustomerRequestException {
		
		return identityService.registerCustomer(customerRequest);
	}
}

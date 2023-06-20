package com.neha.bitespeed.contactIdentity.service;

import com.neha.bitespeed.contactIdentity.dto.CustomerRequestDTO;
import com.neha.bitespeed.contactIdentity.dto.CustomerResponseDTO;
import com.neha.bitespeed.contactIdentity.exception.CustomerAlreadyExistException;
import com.neha.bitespeed.contactIdentity.exception.InvalidCustomerRequestException;

public interface ContactIdentityService {

	CustomerResponseDTO registerCustomer(CustomerRequestDTO customerRequest) throws CustomerAlreadyExistException, InvalidCustomerRequestException;
}

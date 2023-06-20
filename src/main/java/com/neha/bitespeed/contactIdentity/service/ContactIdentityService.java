package com.neha.bitespeed.contactIdentity.service;

import com.neha.bitespeed.contactIdentity.dto.CustomerRequestDTO;
import com.neha.bitespeed.contactIdentity.dto.CustomerResponseDTO;

public interface ContactIdentityService {

	CustomerResponseDTO registerCustomer(CustomerRequestDTO customerRequest);
}

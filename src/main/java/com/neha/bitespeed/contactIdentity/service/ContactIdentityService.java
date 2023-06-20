package com.neha.bitespeed.contactIdentity.service;

import javax.validation.Valid;

import com.neha.bitespeed.contactIdentity.dto.CustomerRequestDTO;
import com.neha.bitespeed.contactIdentity.dto.CustomerResponseDTO;

public interface ContactIdentityService {

	CustomerResponseDTO registerCustomer(@Valid CustomerRequestDTO customerRequest);
}

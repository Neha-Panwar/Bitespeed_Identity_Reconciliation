package com.neha.bitespeed.contactIdentity.service;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neha.bitespeed.contactIdentity.dto.CustomerRequestDTO;
import com.neha.bitespeed.contactIdentity.dto.CustomerResponseDTO;
import com.neha.bitespeed.contactIdentity.model.ContactDetail;
import com.neha.bitespeed.contactIdentity.model.LinkPrecedence;
import com.neha.bitespeed.contactIdentity.repository.ContactDetailRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ContactIdentityServiceImpl implements ContactIdentityService{

	@Autowired
	private ContactDetailRepository contactDetailRepo;
	
	@Override
	public CustomerResponseDTO registerCustomer(CustomerRequestDTO customerRequest) {
		CustomerResponseDTO customerResponse = new CustomerResponseDTO();
		ContactDetail newContact = new ContactDetail();
		
		if(isCustomerRegistered(customerRequest)) {
			
		}
		else {
			log.info("completely new contact details - Primary");
			newContact.setPhoneNumber(customerRequest.getPhoneNumber());
			newContact.setEmail(customerRequest.getEmail());
			newContact.setLinkPrecedence(LinkPrecedence.PRIMARY);
			newContact.setCreatedAt(LocalDateTime.now());
			newContact.setUpdatedAt(LocalDateTime.now());
			
			ContactDetail contactDetail = contactDetailRepo.save(newContact);
			log.info("saved contact with id: {}", contactDetail.getId());
			
			String[] emails = new String[] {contactDetail.getEmail()};
			String[] phoneNumbers = new String[] {contactDetail.getPhoneNumber()};
			
			customerResponse = new CustomerResponseDTO(contactDetail.getId(), Set.of(emails), Set.of(phoneNumbers), new LinkedHashSet<Integer>());
		}
		return customerResponse;
	}

	private boolean isCustomerRegistered(CustomerRequestDTO customerRequest) {
		if(contactDetailRepo.existsByEmail(customerRequest.getEmail()) || contactDetailRepo.existsByPhoneNumber(customerRequest.getPhoneNumber())) {
			return true;
		}
		return false;
	}
	

}

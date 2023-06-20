package com.neha.bitespeed.contactIdentity.service;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Optional;
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
			
			Optional<ContactDetail> contactDetailByEmail = contactDetailRepo.findTopByEmail(customerRequest.getEmail());
			Optional<ContactDetail> contactDetailByPhone = contactDetailRepo.findTopByPhoneNumber(customerRequest.getPhoneNumber());
			
			// 1. when a contact with existing email and new phoneNumber is passed as request
			if(contactDetailByEmail.isPresent() && contactDetailByPhone.isEmpty()) {
				
				ContactDetail linkedContact = contactDetailByEmail.get().getLinkedContact();
				
				if(Optional.ofNullable(linkedContact).isPresent()) {
					log.info("linked contact present for existing contact");
					newContact.setLinkedContact(linkedContact);
				}
				else {
					log.info("linked account not present for existing contact");
					newContact.setLinkedContact(contactDetailByEmail.get());
				}
				
				newContact.setPhoneNumber(customerRequest.getPhoneNumber());
				newContact.setEmail(customerRequest.getEmail());
				newContact.setLinkPrecedence(LinkPrecedence.SECONDARY);
				newContact.setCreatedAt(LocalDateTime.now());
				newContact.setUpdatedAt(LocalDateTime.now());
				
				// to avoid registering contact with null phoneNumber 
				boolean isCustomerPhoneNumberProvided = Optional.ofNullable(customerRequest.getPhoneNumber()).isPresent();
				if(isCustomerPhoneNumberProvided) {
					ContactDetail contactDetail = contactDetailRepo.save(newContact);
					log.info("created Secondary contact with id: {}", contactDetail.getId());
				}
			}
			
			// setting customer response
			Set<String> emails = new LinkedHashSet<>();
			Set<String> phoneNumbers = new LinkedHashSet<>();
			Set<Integer> secondaryContactIds = new LinkedHashSet<>();
			
			ContactDetail primaryContact = contactDetailRepo.findById(newContact.getLinkedContact().getId()).get();
			
			emails.add(primaryContact.getEmail());
			emails.addAll(contactDetailRepo.findAllEmailsById(primaryContact.getId()));
			phoneNumbers.add(primaryContact.getPhoneNumber());
			phoneNumbers.addAll(contactDetailRepo.findAllPhonesById(primaryContact.getId()));
			secondaryContactIds.addAll(contactDetailRepo.findAllLinkedContacts(primaryContact.getId()));
			
			customerResponse.setPrimaryContactId(primaryContact.getId());
			customerResponse.setEmails(emails);
			customerResponse.setPhoneNumbers(phoneNumbers);
			customerResponse.setSecondaryContactIds(secondaryContactIds);
			
		}
		else {
			// completely new contact details for registration - Primary contact
			newContact.setPhoneNumber(customerRequest.getPhoneNumber());
			newContact.setEmail(customerRequest.getEmail());
			newContact.setLinkPrecedence(LinkPrecedence.PRIMARY);
			newContact.setCreatedAt(LocalDateTime.now());
			newContact.setUpdatedAt(LocalDateTime.now());
			
			ContactDetail contactDetail = contactDetailRepo.save(newContact);
			log.info("created Primary contact with id: {}", contactDetail.getId());
			
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

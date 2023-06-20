package com.neha.bitespeed.contactIdentity.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neha.bitespeed.contactIdentity.dto.CustomerRequestDTO;
import com.neha.bitespeed.contactIdentity.dto.CustomerResponseDTO;
import com.neha.bitespeed.contactIdentity.exception.CustomerAlreadyExistException;
import com.neha.bitespeed.contactIdentity.exception.InvalidCustomerRequestException;
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
	public CustomerResponseDTO registerCustomer(CustomerRequestDTO customerRequest) throws CustomerAlreadyExistException, InvalidCustomerRequestException {
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
					log.info("linked contact not present for existing contact");
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
			
			// 2. when a contact with existing phoneNumber and new email is passed as request
			else if(contactDetailByPhone.isPresent() && contactDetailByEmail.isEmpty()) {
				
				ContactDetail linkedContact = contactDetailByPhone.get().getLinkedContact();
				
				if(Optional.ofNullable(linkedContact).isPresent()) {
					log.info("linked contact present for existing contact");
					newContact.setLinkedContact(linkedContact);
				}
				else {
					log.info("linked contact not present for existing contact");
					newContact.setLinkedContact(contactDetailByPhone.get());
				}
				
				newContact.setPhoneNumber(customerRequest.getPhoneNumber());
				newContact.setEmail(customerRequest.getEmail());
				newContact.setLinkPrecedence(LinkPrecedence.SECONDARY);
				newContact.setCreatedAt(LocalDateTime.now());
				newContact.setUpdatedAt(LocalDateTime.now());
				
				// to avoid registering contact with null emailId
				boolean isCustomerEmailProvided = Optional.ofNullable(customerRequest.getEmail()).isPresent();
				if(isCustomerEmailProvided) {
					ContactDetail contactDetail = contactDetailRepo.save(newContact);
					log.info("created Secondary contact with id: {}", contactDetail.getId());
				}
			}
			
			// 3. when a contact with existing phoneNumber and existing email is passed as request
			else if(contactDetailByEmail.isPresent() && contactDetailByPhone.isPresent()) {
				
				ContactDetail linkedContactByEmail = contactDetailByEmail.get().getLinkedContact();
				ContactDetail linkedContactByPhone = contactDetailByPhone.get().getLinkedContact();
				
				// when both contacts are primary, the older contact will remain primary & later one will be changed to secondary
				if(Optional.ofNullable(linkedContactByEmail).isEmpty() && Optional.ofNullable(linkedContactByPhone).isEmpty()) {
					
					ContactDetail oldContact = Collections.min(Arrays.asList(contactDetailByEmail.get(), contactDetailByPhone.get()), Comparator.comparing(c -> c.getId()));
					newContact = Collections.max(Arrays.asList(contactDetailByEmail.get(), contactDetailByPhone.get()), Comparator.comparing(c -> c.getId()));
					
					newContact.setLinkedContact(oldContact);
					newContact.setLinkPrecedence(LinkPrecedence.SECONDARY);
					newContact.setUpdatedAt(LocalDateTime.now());
						
					ContactDetail contactDetail = contactDetailRepo.save(newContact);
					log.info("updated contact with id: {} to secondary", contactDetail.getId());
					
				}
				else {
					throw new CustomerAlreadyExistException("No new info provided. Customers with given details already exist.");
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

	private boolean isCustomerRegistered(CustomerRequestDTO customerRequest) throws InvalidCustomerRequestException {
		if(Optional.ofNullable(customerRequest.getEmail()).isEmpty() && Optional.ofNullable(customerRequest.getPhoneNumber()).isEmpty()) {
			throw new InvalidCustomerRequestException("Invalid Request - Email and Phone number not provided.");
		}
		if(contactDetailRepo.existsByEmail(customerRequest.getEmail()) || contactDetailRepo.existsByPhoneNumber(customerRequest.getPhoneNumber())) {
			return true;
		}
		return false;
	}
	

}

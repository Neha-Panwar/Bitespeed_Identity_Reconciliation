package com.neha.bitespeed.contactIdentity.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class CustomerResponseDTO {
	
	private int primaryContactId;
	private Set<String> emails;
	private Set<String> phoneNumbers;
	private Set<Integer> secondaryContactIds;

}
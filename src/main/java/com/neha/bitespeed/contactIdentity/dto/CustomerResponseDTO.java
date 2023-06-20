package com.neha.bitespeed.contactIdentity.dto;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "model for customer response object")
public class CustomerResponseDTO {
	
	private int primaryContactId;
	private Set<String> emails;
	private Set<String> phoneNumbers;
	private Set<Integer> secondaryContactIds;

}
package com.neha.bitespeed.contactIdentity.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contact")
@Data @AllArgsConstructor @NoArgsConstructor
public class ContactDetail {
	
	@Id 
	@GeneratedValue
	private  int id;
	
	@Pattern(regexp = "[1-9][0-9]{5,}", message = "phoneNumber should have 6 - 10 digits")
	private  String phoneNumber;
	
	@Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
	        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", 
	        message = "enter valid email format")
	private String email;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "linkedId")
	private ContactDetail linkedContact;
	
	@Enumerated(EnumType.STRING)
	private LinkPrecedence linkPrecedence;
	
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime deletedAt;
}

package com.neha.bitespeed.contactIdentity.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.neha.bitespeed.contactIdentity.model.ContactDetail;

@Repository
public interface ContactDetailRepository extends JpaRepository<ContactDetail, Integer>{
	
	boolean existsByPhoneNumber(String phoneNumber);

	boolean existsByEmail(String email);

	Optional<ContactDetail> findTopByEmail(String email);

	Optional<ContactDetail> findTopByPhoneNumber(String phoneNumber);

	@Query("SELECT DISTINCT c.email from ContactDetail c where c.linkedContact.id = ?1 AND c.email IS NOT NULL")
	Set<String> findAllEmailsById(int linkedId);

	@Query("SELECT DISTINCT c.phoneNumber from ContactDetail c where c.linkedContact.id = ?1 AND c.phoneNumber IS NOT NULL")
	Set<String> findAllPhonesById(int id);

	@Query("SELECT DISTINCT c.id from ContactDetail c where c.linkedContact.id = ?1")
	Set<Integer> findAllLinkedContacts(int id);

}

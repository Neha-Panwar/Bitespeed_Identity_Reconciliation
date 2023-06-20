package com.neha.bitespeed.contactIdentity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.neha.bitespeed.contactIdentity.model.ContactDetail;

@Repository
public interface ContactDetailRepository extends JpaRepository<ContactDetail, Integer>{

}

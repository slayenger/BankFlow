package com.bankflow.repositories;

import com.bankflow.entities.ContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContactInfoRepository extends JpaRepository<ContactInfo, UUID> {
    List<ContactInfo> findByPhoneNumberContains(String phoneNumber);

    List<ContactInfo> findByEmailContains(String email);

    boolean existsByEmail(String email);

    ContactInfo findByUser_UserId(UUID userId);

    boolean existsByPhoneNumber(String phoneNumber);

}

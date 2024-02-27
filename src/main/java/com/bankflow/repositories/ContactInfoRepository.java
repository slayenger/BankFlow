package com.bankflow.repositories;

import com.bankflow.entities.ContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContactInfoRepository extends JpaRepository<ContactInfo, UUID> {

/*    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);*/

    List<ContactInfo> findByPhoneNumberContains(String phoneNumber);

    List<ContactInfo> findByEmailContains(String email);

}

package com.bankflow.repositories;

import com.bankflow.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository  extends JpaRepository<User, UUID> {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    Page<User> findByDateOfBirthAfter(Date dateOfBirth, Pageable pageable);

    Page<User> findByFullNameContains(String fullName, Pageable pageable);
}

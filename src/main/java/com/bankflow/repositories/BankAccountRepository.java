package com.bankflow.repositories;

import com.bankflow.entities.BankAccount;
import com.bankflow.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, UUID> {

    BankAccount findByUser(User user);

    BankAccount findByUser_UserId(UUID userId);
}

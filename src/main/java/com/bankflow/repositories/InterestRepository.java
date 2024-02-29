package com.bankflow.repositories;

import com.bankflow.entities.BankAccount;
import com.bankflow.entities.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InterestRepository extends JpaRepository<Interest, UUID> {
    Interest findByBankAccount(BankAccount bankAccount);
}

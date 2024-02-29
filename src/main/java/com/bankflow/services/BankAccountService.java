package com.bankflow.services;

import com.bankflow.entities.BankAccount;
import com.bankflow.repositories.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;


    public void raiseBalance(UUID userId, BigDecimal amount)
    {
        BankAccount bankAccount = bankAccountRepository.findByUser_UserId(userId);
        BigDecimal updatedBalance = bankAccount.getBalance().add(amount);
        bankAccount.setBalance(updatedBalance);
        bankAccountRepository.save(bankAccount);

    }

    public void reduceBalance(UUID userId, BigDecimal amount)
    {
        BankAccount bankAccount = bankAccountRepository.findByUser_UserId(userId);
        BigDecimal updatedBalance = bankAccount.getBalance().subtract(amount);
        bankAccount.setBalance(updatedBalance);
        bankAccountRepository.save(bankAccount);
    }

}

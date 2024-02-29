package com.bankflow.services;


import com.bankflow.dtos.TransactionResponse;
import com.bankflow.entities.BankAccount;
import com.bankflow.entities.Transaction;
import com.bankflow.entities.User;
import com.bankflow.exceptions.InvalidTransactionOperationException;
import com.bankflow.exceptions.NegativeBalanceException;
import com.bankflow.exceptions.UserNotFoundException;
import com.bankflow.repositories.BankAccountRepository;
import com.bankflow.repositories.TransactionRepository;
import com.bankflow.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;

    private final BankAccountService bankAccountService;

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void createTransaction(UUID senderId, UUID receiverId, BigDecimal amount)
    {
        validateReceiverExists(receiverId);
        validateTransactionAmount(amount);

        User sender = userRepository.getReferenceById(senderId);
        User receiver = userRepository.getReferenceById(receiverId);

        BankAccount senderAccount = bankAccountRepository.findByUser(sender);
        BankAccount receiverAccount = bankAccountRepository.findByUser(receiver);

        validateSufficientFunds(senderAccount, amount);

        updateBalancesAndSaveTransaction(senderAccount, receiverAccount, amount);
    }
    private void validateReceiverExists(UUID receiverId) {
        if (!userRepository.existsById(receiverId)) {
            throw new UserNotFoundException("User with id " + receiverId + " not found.");
        }
    }

    private void validateTransactionAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionOperationException("The transfer amount must be greater than zero.");
        }
    }

    private void validateSufficientFunds(BankAccount senderAccount, BigDecimal amount) {
        if (senderAccount.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeBalanceException("You do not have enough funds to carry out this operation.");
        }
    }

    private void updateBalancesAndSaveTransaction(BankAccount senderAccount, BankAccount receiverAccount, BigDecimal amount) {
        bankAccountService.raiseBalance(receiverAccount.getUser().getUserId(), amount);
        bankAccountService.reduceBalance(senderAccount.getUser().getUserId(), amount);

        senderAccount.setUpdatedAt(new Date());
        receiverAccount.setUpdatedAt(new Date());

        List<BankAccount> accountsToSave = new ArrayList<>();
        accountsToSave.add(senderAccount);
        accountsToSave.add(receiverAccount);

        Transaction transaction = new Transaction();
        transaction.setSender(senderAccount);
        transaction.setReceiver(receiverAccount);
        transaction.setAmount(amount);
        transaction.setTimestamp(new Date());

        transactionRepository.saveAndFlush(transaction);
        bankAccountRepository.saveAllAndFlush(accountsToSave);
    }
}

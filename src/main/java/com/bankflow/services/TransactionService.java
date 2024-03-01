package com.bankflow.services;


import com.bankflow.entities.BankAccount;
import com.bankflow.entities.Transaction;
import com.bankflow.entities.User;
import com.bankflow.exceptions.InvalidTransactionOperationException;
import com.bankflow.exceptions.NegativeBalanceException;
import com.bankflow.exceptions.UserNotFoundException;
import com.bankflow.repositories.BankAccountRepository;
import com.bankflow.repositories.ContactInfoRepository;
import com.bankflow.repositories.TransactionRepository;
import com.bankflow.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
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
    private final ContactInfoRepository infoRepository;
    private final BankAccountService bankAccountService;
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);


    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void createTransaction(UUID senderId, UUID receiverId, BigDecimal amount)
    {
        LOGGER.info("Starting transaction...");
        validateReceiverExists(receiverId);
        validateTransactionAmount(amount);

        User sender = userRepository.getReferenceById(senderId);
        User receiver = userRepository.getReferenceById(receiverId);

        BankAccount senderAccount = bankAccountRepository.findByUser(sender);
        BankAccount receiverAccount = bankAccountRepository.findByUser(receiver);

        validateSufficientFunds(senderAccount, amount);

        updateBalancesAndSaveTransaction(senderAccount, receiverAccount, amount);

        LOGGER.info("Transaction completed successfully.");
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void createTransaction(UUID senderId, String phoneNumber, BigDecimal amount)
    {
        LOGGER.info("Starting transaction...");

        UUID receiverId = infoRepository.findByPhoneNumber(phoneNumber).getUser().getUserId();

        validateReceiverExists(receiverId);
        validateTransactionAmount(amount);

        User sender = userRepository.getReferenceById(senderId);
        User receiver = userRepository.getReferenceById(receiverId);

        BankAccount senderAccount = bankAccountRepository.findByUser(sender);
        BankAccount receiverAccount = bankAccountRepository.findByUser(receiver);

        validateSufficientFunds(senderAccount, amount);

        updateBalancesAndSaveTransaction(senderAccount, receiverAccount, amount);

        LOGGER.info("Transaction completed successfully.");
    }

    private void validateReceiverExists(UUID receiverId) {
        if (!userRepository.existsById(receiverId)) {
            String errorMessage = "User with id " + receiverId + " not found.";
            LOGGER.error(errorMessage);
            throw new UserNotFoundException(errorMessage);
        }
    }

    private void validateTransactionAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            String errorMessage = "The transfer amount must be greater than zero.";
            LOGGER.error(errorMessage);
            throw new InvalidTransactionOperationException(errorMessage);
        }
    }

    private void validateSufficientFunds(BankAccount senderAccount, BigDecimal amount) {
        if (senderAccount.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            String errorMessage = "You do not have enough funds to carry out this operation.";
            LOGGER.error(errorMessage);
            throw new NegativeBalanceException(errorMessage);
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

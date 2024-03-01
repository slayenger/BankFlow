package com.bankflow.services;

import com.bankflow.entities.BankAccount;
import com.bankflow.entities.Transaction;
import com.bankflow.entities.User;
import com.bankflow.exceptions.InvalidTransactionOperationException;
import com.bankflow.exceptions.NegativeBalanceException;
import com.bankflow.exceptions.UserNotFoundException;
import com.bankflow.repositories.BankAccountRepository;
import com.bankflow.repositories.TransactionRepository;
import com.bankflow.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private BankAccountService bankAccountService;

    @InjectMocks
    private TransactionService transactionService;


    @Test
    public void testCreateTransaction_ValidTransaction() {
        UUID senderId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(100);

        User sender = new User();
        sender.setUserId(senderId);

        User receiver = new User();
        receiver.setUserId(receiverId);

        BankAccount senderAccount = new BankAccount();
        senderAccount.setUser(sender);
        senderAccount.setBalance(BigDecimal.valueOf(500));

        BankAccount receiverAccount = new BankAccount();
        receiverAccount.setUser(receiver);
        receiverAccount.setBalance(BigDecimal.valueOf(200));

        when(userRepository.existsById(receiverId)).thenReturn(true);
        when(userRepository.getReferenceById(senderId)).thenReturn(sender);
        when(userRepository.getReferenceById(receiverId)).thenReturn(receiver);
        when(bankAccountRepository.findByUser(sender)).thenReturn(senderAccount);
        when(bankAccountRepository.findByUser(receiver)).thenReturn(receiverAccount);

        transactionService.createTransaction(senderId, receiverId, amount);

        verify(bankAccountService).raiseBalance(receiverId, amount);
        verify(bankAccountService).reduceBalance(senderId, amount);
        verify(transactionRepository).saveAndFlush(any(Transaction.class));
        verify(bankAccountRepository).saveAllAndFlush(anyList());
    }

    @Test
    public void testCreateTransaction_ReceiverNotFound() {
        UUID senderId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(100);

        when(userRepository.existsById(receiverId)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () ->
                transactionService.createTransaction(senderId, receiverId, amount));
    }

    @Test
    public void testCreateTransaction_InvalidTransactionOperation()
    {
        UUID senderId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(-100);

        when(userRepository.existsById(receiverId)).thenReturn(true);
        assertThrows(InvalidTransactionOperationException.class, () ->
                transactionService.createTransaction(senderId, receiverId, amount));
    }

    @Test
    public  void testCreateTransaction_NegativeBalance()
    {
        UUID senderId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(100);

        User sender = new User();
        sender.setUserId(senderId);

        User receiver = new User();
        receiver.setUserId(receiverId);

        BankAccount senderAccount = new BankAccount();
        senderAccount.setUser(sender);
        senderAccount.setBalance(BigDecimal.valueOf(50));

        BankAccount receiverAccount = new BankAccount();
        receiverAccount.setUser(receiver);
        receiverAccount.setBalance(BigDecimal.valueOf(200));

        when(userRepository.existsById(receiverId)).thenReturn(true);
        when(userRepository.getReferenceById(senderId)).thenReturn(sender);
        when(userRepository.getReferenceById(receiverId)).thenReturn(receiver);
        when(bankAccountRepository.findByUser(sender)).thenReturn(senderAccount);
        when(bankAccountRepository.findByUser(receiver)).thenReturn(receiverAccount);

        assertThrows(NegativeBalanceException.class, () ->
                transactionService.createTransaction(senderId, receiverId, amount));
    }
}
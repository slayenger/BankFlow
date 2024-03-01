package com.bankflow.controllers;

import com.bankflow.dtos.CustomUserDetails;
import com.bankflow.dtos.TransactionNumberRequest;
import com.bankflow.dtos.TransactionRequest;
import com.bankflow.exceptions.InvalidTransactionOperationException;
import com.bankflow.exceptions.NegativeBalanceException;
import com.bankflow.exceptions.UserNotFoundException;
import com.bankflow.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/send-money")
    public ResponseEntity<?> sendMoneyToUser(@RequestBody TransactionRequest request,
                                             @AuthenticationPrincipal CustomUserDetails sender)
    {
        UUID receiverId = request.getReceiverId();
        BigDecimal amount = request.getAmount();
        try
        {
            transactionService.createTransaction(sender.getUserId(), receiverId, amount);
            return ResponseEntity.status(HttpStatus.CREATED).body("The operation was completed successfully");
        }
        catch (UserNotFoundException | NegativeBalanceException | InvalidTransactionOperationException err)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
        }
    }

    @PostMapping("/send-money-by-number")
    public ResponseEntity<?> sendMoneyToUserByNumber(@RequestBody TransactionNumberRequest request,
                                                     @AuthenticationPrincipal CustomUserDetails sender)
    {
        String phoneNumber = request.getPhoneNumber();
        BigDecimal amount = request.getAmount();
        try
        {
            transactionService.createTransaction(sender.getUserId(), phoneNumber, amount);
            return ResponseEntity.status(HttpStatus.CREATED).body("The operation was completed successfully");
        }
        catch (UserNotFoundException | NegativeBalanceException | InvalidTransactionOperationException err)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
        }
    }


}

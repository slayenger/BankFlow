package com.bankflow.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TransactionRequest {

    private UUID receiverId;
    private BigDecimal amount;

}

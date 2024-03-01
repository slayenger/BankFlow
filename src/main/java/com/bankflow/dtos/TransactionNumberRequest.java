package com.bankflow.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionNumberRequest {

    private String phoneNumber;
    private BigDecimal amount;

}

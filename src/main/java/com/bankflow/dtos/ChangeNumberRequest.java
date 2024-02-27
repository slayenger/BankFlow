package com.bankflow.dtos;

import lombok.Data;

@Data
public class ChangeNumberRequest {

    private String currentNumber;
    private String newNumber;

}

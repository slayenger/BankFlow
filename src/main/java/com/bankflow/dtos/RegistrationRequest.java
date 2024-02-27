package com.bankflow.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

    private String username;
    private String password;
    private BigDecimal initialAmount;
    private String phoneNumber;
    private String email;

}

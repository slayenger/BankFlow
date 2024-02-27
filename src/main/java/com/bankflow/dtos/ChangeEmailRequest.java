package com.bankflow.dtos;

import lombok.Data;

@Data
public class ChangeEmailRequest {

    private String currentEmail;
    private String newEmail;
}

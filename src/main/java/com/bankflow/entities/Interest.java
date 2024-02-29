package com.bankflow.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "interest")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID interestId;

    @OneToOne
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;

    private BigDecimal initialBalance;

    private Date timestamp;

}

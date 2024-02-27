package com.bankflow.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "transaction")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transactionId;

    @OneToOne
    @JoinColumn(name = "sender_bank_account_id")
    private BankAccount sender;

    @OneToOne
    @JoinColumn(name = "receiver_bank_account_id")
    private BankAccount receiver;

    private BigDecimal amount;

    private Date timestamp;

}

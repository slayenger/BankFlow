package com.bankflow.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "contact_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID contactInfoId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String phoneNumber;

    private String email;

    private Date createdAt;

    private Date updatedAt;
}

package com.bankflow.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
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

    @ElementCollection
    @CollectionTable(name = "phone_numbers", joinColumns = @JoinColumn(name = "contact_info_id"))
    private Set<String> phoneNumber = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "emails", joinColumns = @JoinColumn(name = "contact_info_id"))
    private Set<String> email = new HashSet<>();


    private Date createdAt;

    private Date updatedAt;
}

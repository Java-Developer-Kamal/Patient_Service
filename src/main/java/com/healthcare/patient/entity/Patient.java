package com.healthcare.patient.entity;


import com.healthcare.patient.enums.AccountStatus;
import com.healthcare.patient.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patients", indexes = {
        @Index(name = "idx_email", columnList = "email", unique = true),
        @Index(name = "idx_phone", columnList = "phone_number"),
        @Index(name = "idx_insurance", columnList = "insurance_number")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Personal Info ---
    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Gender gender;

    // --- Address Info ---
    @Column(length = 500)
    private String address;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(name = "zip_code", length = 10)
    private String zipCode;

    @Column(length = 50)
    private String country;

    // --- Insurance ---
    @Column(name = "insurance_provider", length = 100)
    private String insuranceProvider;

    @Column(name = "insurance_number", length = 50)
    private String insuranceNumber;

    // --- Relationships ---

    // 1. Emergency Contacts (Kept here because they are part of User Profile data)
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EmergencyContact> emergencyContacts = new ArrayList<>();

    // --- Audit & System Fields ---
    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false)
    @Builder.Default
    private AccountStatus accountStatus = AccountStatus.ACTIVE;

    @Column(name = "email_verified", nullable = false)
    @Builder.Default
    private Boolean emailVerified = false;

    @Column(name = "phone_verified", nullable = false)
    @Builder.Default
    private Boolean phoneVerified = false;

    @Version // Optimistic Locking (Great practice!)
    private Long version;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Helper for Bidirectional mapping
    public void addEmergencyContact(EmergencyContact contact) {
        emergencyContacts.add(contact);
        contact.setPatient(this);
    }
}
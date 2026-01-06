package com.healthcare.patient.dto.response;

import com.healthcare.patient.enums.AccountStatus;
import com.healthcare.patient.enums.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record PatientResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        LocalDate dateOfBirth,
        Gender gender,

        // Address Info
        String address,
        String city,
        String state,
        String zipCode,
        String country,

        // Insurance
        String insuranceProvider,
        String insuranceNumber,

        // Relationships
        List<EmergencyContactResponse> emergencyContacts,

        // System Fields (ReadOnly)
        AccountStatus accountStatus,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}

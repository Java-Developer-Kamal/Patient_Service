package com.healthcare.patient.dto.response;

public record EmergencyContactResponse(
        Long id,
        String name,
        String relationship,
        String phone
) {}

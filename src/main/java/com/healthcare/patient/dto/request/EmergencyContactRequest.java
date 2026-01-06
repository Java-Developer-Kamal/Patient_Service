package com.healthcare.patient.dto.request;
import com.healthcare.patient.constant.PatientConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EmergencyContactRequest(
        @NotBlank(message = PatientConstants.MSG_CONTACT_NAME_REQUIRED)
        String name,

        @NotBlank(message = PatientConstants.MSG_RELATION_REQUIRED)
        String relationship,

        @NotBlank(message = PatientConstants.MSG_CONTACT_PHONE_REQUIRED)
        @Pattern(regexp = PatientConstants.PHONE_REGEX, message = PatientConstants.MSG_PHONE_INVALID)
        String phone
) {}
package com.healthcare.patient.dto.request;

import com.healthcare.patient.enums.Gender;
import com.healthcare.patient.constant.PatientConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
public record PatientRequest(
        @NotBlank(message = PatientConstants.MSG_FIRST_NAME_REQUIRED)
        @Size(min = 2, max = 100)
        String firstName,

        @NotBlank(message = PatientConstants.MSG_LAST_NAME_REQUIRED)
        @Size(min = 2, max = 100)
        String lastName,

        @NotBlank(message = PatientConstants.MSG_EMAIL_REQUIRED)
        @Email(message = PatientConstants.MSG_EMAIL_INVALID)
        String email,

        @NotBlank(message = PatientConstants.MSG_PHONE_REQUIRED)
        @Pattern(regexp = PatientConstants.PHONE_REGEX, message = PatientConstants.MSG_PHONE_INVALID)
        String phoneNumber,

        @NotNull(message = PatientConstants.MSG_DOB_REQUIRED)
        @Past(message = PatientConstants.MSG_DOB_PAST)
        LocalDate dateOfBirth,

        @NotNull(message = PatientConstants.MSG_GENDER_REQUIRED)
        Gender gender,

        @Size(max = 500) String address,
        @Size(max = 100) String city,
        @Size(max = 100) String state,
        @Pattern(regexp = PatientConstants.ZIP_CODE_REGEX, message = PatientConstants.MSG_ZIP_INVALID)
        String zipCode,
        String country,

        String insuranceProvider,
        String insuranceNumber,

        @Valid
        List<EmergencyContactRequest> emergencyContacts
) {}
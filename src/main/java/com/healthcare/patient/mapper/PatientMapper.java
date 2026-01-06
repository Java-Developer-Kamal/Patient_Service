package com.healthcare.patient.mapper;

import com.healthcare.patient.dto.response.EmergencyContactResponse;
import com.healthcare.patient.entity.EmergencyContact;
import com.healthcare.patient.entity.Patient;

import com.healthcare.patient.dto.request.PatientRequest;
import com.healthcare.patient.dto.response.PatientResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface PatientMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "accountStatus", ignore = true)

    //  ADDED: Explicitly ignore internal flags to fix warnings
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "phoneVerified", ignore = true)

    // CRITICAL: We handle this manually in the Service to set the Parent ID correctly
    @Mapping(target = "emergencyContacts", ignore = true)
    Patient toEntity(PatientRequest request);

    PatientResponse toResponse(Patient patient);

    // Helper for the list mapping inside PatientResponse
    EmergencyContactResponse toContactResponse(EmergencyContact contact);
}
package com.healthcare.patient.service.impl;

import com.healthcare.patient.dto.request.PatientRequest;
import com.healthcare.patient.dto.response.PatientResponse;
import com.healthcare.patient.entity.EmergencyContact;
import com.healthcare.patient.entity.Patient;
import com.healthcare.patient.handler.DuplicateResourceException;
import com.healthcare.patient.handler.ResourceNotFoundException;
import com.healthcare.patient.mapper.PatientMapper;
import com.healthcare.patient.repository.PatientRepository;
import com.healthcare.patient.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repository;
    private final PatientMapper mapper;

    @Override
    public PatientResponse registerPatient(PatientRequest request) {
        log.info("Registering new patient: {}", request.email());

        // 1. Check Duplicates
        if (repository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already exists: " + request.email());
        }
        if (repository.existsByPhoneNumber(request.phoneNumber())) {
            throw new DuplicateResourceException("Phone number already exists: " + request.phoneNumber());
        }

        Patient patient = mapper.toEntity(request);

        // 3. Manually Link Emergency Contacts (To set the Parent)
        if (request.emergencyContacts() != null) {
            request.emergencyContacts().forEach(c -> {
                EmergencyContact contact = EmergencyContact.builder()
                        .name(c.name())
                        .phone(c.phone())
                        .relationship(c.relationship())
                        .build();
                patient.addEmergencyContact(contact); // This sets contact.setPatient(patient)
            });
        }

        Patient savedPatient = repository.save(patient);
        log.info("Patient created with ID: {}", savedPatient.getId());

        return mapper.toResponse(savedPatient);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponse getPatient(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + id));
    }
}
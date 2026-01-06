package com.healthcare.patient.service;

import com.healthcare.patient.dto.request.EmergencyContactRequest;
import com.healthcare.patient.dto.request.PatientRequest;
import com.healthcare.patient.dto.response.EmergencyContactResponse;
import com.healthcare.patient.dto.response.PatientResponse;
import com.healthcare.patient.entity.Patient;
import com.healthcare.patient.enums.AccountStatus;
import com.healthcare.patient.enums.Gender;
import com.healthcare.patient.handler.DuplicateResourceException;
import com.healthcare.patient.handler.ResourceNotFoundException;
import com.healthcare.patient.mapper.PatientMapper;
import com.healthcare.patient.repository.PatientRepository;
import com.healthcare.patient.service.impl.PatientServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock
    private PatientRepository repository;

    @Mock
    private PatientMapper mapper;

    @InjectMocks
    private PatientServiceImpl patientService;


    @Test
    @DisplayName("registerPatient - Success Scenario")
    void registerPatient_Success() {
        // 1. ARRANGE
        PatientRequest request = createMockRequest();
        Patient patientEntity = new Patient(); // Mock entity
        patientEntity.setId(null); // ID is null before save

        Patient savedPatient = new Patient();
        savedPatient.setId(1L);
        savedPatient.setEmail("john.doe@example.com");

        PatientResponse expectedResponse = createMockResponse(1L);

        // Mock Behavior
        when(repository.existsByEmail(request.email())).thenReturn(false);
        when(repository.existsByPhoneNumber(request.phoneNumber())).thenReturn(false);
        when(mapper.toEntity(request)).thenReturn(patientEntity);
        when(repository.save(any(Patient.class))).thenReturn(savedPatient);
        when(mapper.toResponse(savedPatient)).thenReturn(expectedResponse);

        // 2. ACT
        PatientResponse result = patientService.registerPatient(request);

        // 3. ASSERT
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("john.doe@example.com", result.email());
        assertEquals(Gender.MALE, result.gender());

        verify(repository).save(patientEntity);
    }

    @Test
    @DisplayName("registerPatient - Should throw Exception on Duplicate Email")
    void registerPatient_DuplicateEmail() {
        // 1. ARRANGE
        PatientRequest request = createMockRequest();
        when(repository.existsByEmail(request.email())).thenReturn(true);

        // 2. ACT & ASSERT
        DuplicateResourceException ex = assertThrows(DuplicateResourceException.class, () ->
                patientService.registerPatient(request)
        );

        assertTrue(ex.getMessage().contains("Email already exists"));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("registerPatient - Should throw Exception on Duplicate Phone")
    void registerPatient_DuplicatePhone() {
        // 1. ARRANGE
        PatientRequest request = createMockRequest();
        when(repository.existsByEmail(request.email())).thenReturn(false);
        when(repository.existsByPhoneNumber(request.phoneNumber())).thenReturn(true);

        // 2. ACT & ASSERT
        DuplicateResourceException ex = assertThrows(DuplicateResourceException.class, () ->
                patientService.registerPatient(request)
        );

        assertTrue(ex.getMessage().contains("Phone number already exists"));
        verify(repository, never()).save(any());
    }

    // --- GET PATIENT TESTS ---

    @Test
    @DisplayName("getPatient - Success Scenario")
    void getPatient_Success() {
        // 1. ARRANGE
        Long patientId = 1L;
        Patient patient = new Patient();
        patient.setId(patientId);

        PatientResponse expectedResponse = createMockResponse(patientId);

        when(repository.findById(patientId)).thenReturn(Optional.of(patient));
        when(mapper.toResponse(patient)).thenReturn(expectedResponse);

        // 2. ACT
        PatientResponse result = patientService.getPatient(patientId);

        // 3. ASSERT
        assertNotNull(result);
        assertEquals(patientId, result.id());
        verify(repository).findById(patientId);
    }

    @Test
    @DisplayName("getPatient - Should throw Exception when ID not found")
    void getPatient_NotFound() {
        // 1. ARRANGE
        Long patientId = 99L;
        when(repository.findById(patientId)).thenReturn(Optional.empty());

        // 2. ACT & ASSERT
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                patientService.getPatient(patientId)
        );

        assertTrue(ex.getMessage().contains("Patient not found"));
    }


    private PatientRequest createMockRequest() {
        // Inner Record for Contact
        EmergencyContactRequest contact = new EmergencyContactRequest(
                "Jane Doe", "9876543210", "Sister"
        );

        return new PatientRequest(
                "John",
                "Doe",
                "john.doe@example.com",
                "1234567890",
                LocalDate.of(1990, 1, 1),
                Gender.MALE,
                "123 Main St",
                "New York",
                "NY",
                "10001",
                "USA",
                "BlueCross",
                "INS-123456",
                List.of(contact) // List<EmergencyContactRequest>
        );
    }

    private PatientResponse createMockResponse(Long id) {
        // Inner Record for Contact Response
        EmergencyContactResponse contactResp = new EmergencyContactResponse(
                1L, "Jane Doe", "9876543210", "Sister"
        );

        return new PatientResponse(
                id,
                "John",
                "Doe",
                "john.doe@example.com",
                "1234567890",
                LocalDate.of(1990, 1, 1),
                Gender.MALE,
                "123 Main St",
                "New York",
                "NY",
                "10001",
                "USA",
                "BlueCross",
                "INS-123456",
                List.of(contactResp),
                AccountStatus.ACTIVE,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
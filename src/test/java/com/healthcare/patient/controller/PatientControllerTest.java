package com.healthcare.patient.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.patient.constant.PatientConstants;
import com.healthcare.patient.dto.request.EmergencyContactRequest;
import com.healthcare.patient.dto.request.PatientRequest;
import com.healthcare.patient.dto.response.PatientResponse;
import com.healthcare.patient.enums.AccountStatus;
import com.healthcare.patient.enums.Gender;
import com.healthcare.patient.handler.DuplicateResourceException;
import com.healthcare.patient.handler.ResourceNotFoundException;
import com.healthcare.patient.service.PatientService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // To convert Objects -> JSON string

    @MockBean
    private PatientService patientService;



    @Test
    @DisplayName("POST /api/v1/patients - Success")
    void registerPatient_Success() throws Exception {
        // 1. ARRANGE
        PatientRequest request = createMockRequest();
        PatientResponse mockResponse = createMockResponse(1L);

        when(patientService.registerPatient(any(PatientRequest.class))).thenReturn(mockResponse);

        // 2. ACT & ASSERT
        mockMvc.perform(post("/api/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))) // Convert Request to JSON
                .andExpect(status().isCreated()) // Expect 201
                .andExpect(jsonPath("$.success").value(true)) // Check ApiResponse wrapper
                .andExpect(jsonPath("$.message").value(PatientConstants.SUCCESS_REGISTER))
                .andExpect(jsonPath("$.data.email").value("john.doe@example.com")) // Check Payload
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    @DisplayName("POST /api/v1/patients - Validation Error (Bad Request)")
    void registerPatient_ValidationError() throws Exception {
        // 1. ARRANGE - Create request with INVALID email (Blank)
        PatientRequest invalidRequest = new PatientRequest(
                "John", "Doe", "", "1234567890", // Email is blank
                LocalDate.of(1990, 1, 1), Gender.MALE,
                null, null, null, null, null, null, null, null
        );

        // 2. ACT & ASSERT
        mockMvc.perform(post("/api/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest()); // Expect 400
        // NOTE: If you have a GlobalExceptionHandler, you can verify the error message too
        // .andExpect(jsonPath("$.errors.email").exists());
    }

    @Test
    @DisplayName("POST /api/v1/patients - Duplicate Resource (Conflict)")
    void registerPatient_DuplicateEmail() throws Exception {
        // 1. ARRANGE
        PatientRequest request = createMockRequest();

        // Simulate Service throwing Duplicate Exception
        when(patientService.registerPatient(any(PatientRequest.class)))
                .thenThrow(new DuplicateResourceException("Email already exists"));

        // 2. ACT & ASSERT
        mockMvc.perform(post("/api/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict()) // Expect 409
                .andExpect(jsonPath("$.success").value(false));
    }

    // --- GET PATIENT (GET) TESTS ---

    @Test
    @DisplayName("GET /api/v1/patients/{id} - Success")
    void getPatient_Success() throws Exception {
        // 1. ARRANGE
        Long patientId = 1L;
        PatientResponse mockResponse = createMockResponse(patientId);

        when(patientService.getPatient(patientId)).thenReturn(mockResponse);

        // 2. ACT & ASSERT
        mockMvc.perform(get("/api/v1/patients/{id}", patientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expect 200
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(PatientConstants.SUCCESS_RETRIEVE))
                .andExpect(jsonPath("$.data.firstName").value("John"));
    }

    @Test
    @DisplayName("GET /api/v1/patients/{id} - Not Found")
    void getPatient_NotFound() throws Exception {
        // 1. ARRANGE
        Long patientId = 99L;
        when(patientService.getPatient(patientId))
                .thenThrow(new ResourceNotFoundException("Patient not found"));

        // 2. ACT & ASSERT
        mockMvc.perform(get("/api/v1/patients/{id}", patientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Expect 404
    }

    private PatientRequest createMockRequest() {
        EmergencyContactRequest contact = new EmergencyContactRequest("Jane", "Sister", "9876543210");
        return new PatientRequest(
                "John", "Doe", "john.doe@example.com", "1234567890",
                LocalDate.of(1990, 1, 1), Gender.MALE,
                "123 Main St", "NY", "NY", "10001", "USA",
                "InsureCo", "123", List.of(contact)
        );
    }

    private PatientResponse createMockResponse(Long id) {
        return new PatientResponse(
                id, "John", "Doe", "john.doe@example.com", "1234567890",
                LocalDate.of(1990, 1, 1), Gender.MALE,
                "123 Main St", "NY", "NY", "10001", "USA",
                "InsureCo", "123", Collections.emptyList(),
                AccountStatus.ACTIVE, LocalDateTime.now(), LocalDateTime.now()
        );
    }
}
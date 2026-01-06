package com.healthcare.patient.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.patient.constant.FeedbackConstants;
import com.healthcare.patient.dto.request.FeedbackRequest;
import com.healthcare.patient.dto.response.FeedbackResponse;
import com.healthcare.patient.handler.ResourceNotFoundException;
import com.healthcare.patient.service.impl.FeedbackServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FeedbackController.class) // Only loads the Web Layer
class FeedbackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FeedbackServiceImpl feedbackService;


    @Test
    @DisplayName("POST /api/v1/patients/{id}/feedback - Success (201 Created)")
    void submitFeedback_Success() throws Exception {
        // 1. ARRANGE
        Long patientId = 1L;
        FeedbackRequest request = new FeedbackRequest(5, "Excellent Service");

        FeedbackResponse mockResponse = new FeedbackResponse(
                10L, 5, "Excellent Service", LocalDateTime.now()
        );

        when(feedbackService.submitFeedback(eq(patientId), any(FeedbackRequest.class)))
                .thenReturn(mockResponse);

        // 2. ACT & ASSERT
        mockMvc.perform(post("/api/v1/patients/{patientId}/feedback", patientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated()) // Expect HTTP 201
                .andExpect(jsonPath("$.success").value(true)) // Check ApiResponse wrapper
                .andExpect(jsonPath("$.message").value(FeedbackConstants.SUCCESS_SUBMIT))
                .andExpect(jsonPath("$.data.id").value(10))
                .andExpect(jsonPath("$.data.rating").value(5));
    }

    @Test
    @DisplayName("POST /api/v1/patients/{id}/feedback - Validation Error (400 Bad Request)")
    void submitFeedback_InvalidInput() throws Exception {

        FeedbackRequest invalidRequest = new FeedbackRequest(6, "");

        mockMvc.perform(post("/api/v1/patients/{patientId}/feedback", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest()) // Expect HTTP 400
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("POST /api/v1/patients/{id}/feedback - Patient Not Found (404)")
    void submitFeedback_PatientNotFound() throws Exception {
        // 1. ARRANGE
        Long patientId = 999L;
        FeedbackRequest request = new FeedbackRequest(4, "Good");

        when(feedbackService.submitFeedback(eq(patientId), any(FeedbackRequest.class)))
                .thenThrow(new ResourceNotFoundException("Patient not found"));

        // 2. ACT & ASSERT
        mockMvc.perform(post("/api/v1/patients/{patientId}/feedback", patientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound()) // Expect HTTP 404
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Patient not found"));
    }


    @Test
    @DisplayName("GET /api/v1/patients/{id}/feedback - Success (200 OK)")
    void getPatientFeedback_Success() throws Exception {
        // 1. ARRANGE
        Long patientId = 1L;

        FeedbackResponse item1 = new FeedbackResponse(10L, 5, "Great", LocalDateTime.now());
        FeedbackResponse item2 = new FeedbackResponse(11L, 4, "Good", LocalDateTime.now());

        Page<FeedbackResponse> mockPage = new PageImpl<>(List.of(item1, item2));

        // Note: Use any(Pageable.class) because Spring converts request params to Pageable
        when(feedbackService.getFeedbackByPatient(eq(patientId), any(Pageable.class)))
                .thenReturn(mockPage);

        // 2. ACT & ASSERT
        mockMvc.perform(get("/api/v1/patients/{patientId}/feedback", patientId)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expect HTTP 200
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(FeedbackConstants.SUCCESS_HISTORY))
                // Verify Page content inside "data"
                .andExpect(jsonPath("$.data.content[0].rating").value(5))
                .andExpect(jsonPath("$.data.totalElements").value(2));
    }

    @Test
    @DisplayName("GET /api/v1/patients/{id}/feedback - Empty History (200 OK)")
    void getPatientFeedback_Empty() throws Exception {
        // 1. ARRANGE
        Long patientId = 1L;
        Page<FeedbackResponse> emptyPage = Page.empty();

        when(feedbackService.getFeedbackByPatient(eq(patientId), any(Pageable.class)))
                .thenReturn(emptyPage);

        // 2. ACT & ASSERT
        mockMvc.perform(get("/api/v1/patients/{patientId}/feedback", patientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isEmpty())
                .andExpect(jsonPath("$.data.totalElements").value(0));
    }
}
package com.healthcare.patient.service;


import com.healthcare.patient.constant.FeedbackConstants;
import com.healthcare.patient.dto.request.FeedbackRequest;
import com.healthcare.patient.dto.response.FeedbackResponse;
import com.healthcare.patient.entity.Feedback;
import com.healthcare.patient.entity.Patient;
import com.healthcare.patient.handler.ResourceNotFoundException;
import com.healthcare.patient.mapper.FeedbackMapper;
import com.healthcare.patient.repository.FeedbackRepository;
import com.healthcare.patient.repository.PatientRepository;
import com.healthcare.patient.service.impl.FeedbackServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceImplTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private FeedbackMapper mapper;

    @InjectMocks
    private FeedbackServiceImpl feedbackService;


    @Test
    @DisplayName("submitFeedback - Success Scenario")
    void submitFeedback_Success() {
        // 1. ARRANGE
        Long patientId = 1L;

        // ✅ FIX: Instantiate Record directly (Records don't have setters)
        // Adjust these arguments (5, "Great") to match your Record's fields!
        FeedbackRequest request = new FeedbackRequest(5, "Great Service");

        Patient patient = new Patient();
        patient.setId(patientId);

        Feedback feedbackEntity = new Feedback();
        Feedback savedFeedback = new Feedback();
        savedFeedback.setId(10L);

        // Assume Response is a Class with setters (standard DTO).
        // If Response is ALSO a record, use new FeedbackResponse(...) instead.
        FeedbackResponse expectedResponse = new FeedbackResponse(
                10L,
                5,
                "Great Service",
                LocalDateTime.now()
        );

        // Mocking behavior
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(mapper.toEntity(request)).thenReturn(feedbackEntity);
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(savedFeedback);
        when(mapper.toResponse(savedFeedback)).thenReturn(expectedResponse);

        // 2. ACT
        FeedbackResponse result = feedbackService.submitFeedback(patientId, request);

        // 3. ASSERT
        assertNotNull(result);
        assertEquals(10L, result.id());

        verify(patientRepository).findById(patientId);
        verify(feedbackRepository).save(feedbackEntity);
    }

    @Test
    @DisplayName("submitFeedback - Should throw Exception when Patient not found")
    void submitFeedback_PatientNotFound() {
        // 1. ARRANGE
        Long patientId = 999L;

        FeedbackRequest request = new FeedbackRequest(1, "Poor Service");

        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        // 2. ACT & ASSERT
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                feedbackService.submitFeedback(patientId, request)
        );

        assertTrue(exception.getMessage().contains(FeedbackConstants.ERR_PATIENT_NOT_FOUND));

        verify(feedbackRepository, never()).save(any());
    }



    @Test
    @DisplayName("getFeedbackByPatient - Success Scenario")
    void getFeedbackByPatient_Success() {
        // 1. ARRANGE
        Long patientId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Feedback feedback = new Feedback();
        FeedbackResponse response = new FeedbackResponse(
                20L,
                4,
                "Good",
                LocalDateTime.now()
        );

        Page<Feedback> feedbackPage = new PageImpl<>(List.of(feedback));

        when(patientRepository.existsById(patientId)).thenReturn(true);
        when(feedbackRepository.findByPatientId(patientId, pageable)).thenReturn(feedbackPage);
        when(mapper.toResponse(any(Feedback.class))).thenReturn(response);

        // 2. ACT
        Page<FeedbackResponse> result = feedbackService.getFeedbackByPatient(patientId, pageable);

        // 3. ASSERT
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(mapper, times(1)).toResponse(any());
    }

    @Test
    @DisplayName("getFeedbackByPatient - Should throw Exception when Patient does not exist")
    void getFeedbackByPatient_PatientNotFound() {
        // 1. ARRANGE
        Long patientId = 999L;
        Pageable pageable = PageRequest.of(0, 10);

        when(patientRepository.existsById(patientId)).thenReturn(false);

        // 2. ACT & ASSERT
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                feedbackService.getFeedbackByPatient(patientId, pageable)
        );

        assertEquals(FeedbackConstants.ERR_PATIENT_NOT_FOUND + patientId, exception.getMessage());

        verify(feedbackRepository, never()).findByPatientId(anyLong(), any());
    }
}
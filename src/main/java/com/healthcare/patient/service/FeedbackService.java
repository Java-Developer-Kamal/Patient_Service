package com.healthcare.patient.service;

import com.healthcare.patient.dto.request.FeedbackRequest;
import com.healthcare.patient.dto.response.FeedbackResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedbackService {

     FeedbackResponse submitFeedback(Long patientId, FeedbackRequest request);

     Page<FeedbackResponse> getFeedbackByPatient(Long patientId, Pageable pageable);
}

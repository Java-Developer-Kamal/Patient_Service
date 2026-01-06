package com.healthcare.patient.service.impl;

import com.healthcare.patient.constant.FeedbackConstants;
import com.healthcare.patient.dto.request.FeedbackRequest;
import com.healthcare.patient.dto.response.FeedbackResponse;
import com.healthcare.patient.entity.Feedback;
import com.healthcare.patient.entity.Patient;
import com.healthcare.patient.handler.ResourceNotFoundException;
import com.healthcare.patient.mapper.FeedbackMapper;
import com.healthcare.patient.repository.FeedbackRepository;
import com.healthcare.patient.repository.PatientRepository;
import com.healthcare.patient.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final PatientRepository patientRepository;
    private final FeedbackMapper mapper;

    @Override
    public FeedbackResponse submitFeedback(Long patientId, FeedbackRequest request) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException(FeedbackConstants.ERR_PATIENT_NOT_FOUND + patientId));

        Feedback feedback = mapper.toEntity(request);
        feedback.setPatient(patient);

        Feedback savedFeedback = feedbackRepository.save(feedback);
        log.info("Feedback received from patient ID: {}", patientId);

        return mapper.toResponse(savedFeedback);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedbackResponse> getFeedbackByPatient(Long patientId, Pageable pageable) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException(FeedbackConstants.ERR_PATIENT_NOT_FOUND+ patientId);
        }
        return feedbackRepository.findByPatientId(patientId, pageable)
                .map(mapper::toResponse);
    }
}
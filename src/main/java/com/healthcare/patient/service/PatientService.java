package com.healthcare.patient.service;

import com.healthcare.patient.dto.request.HealthMetricRequest;
import com.healthcare.patient.dto.request.PatientRequest;
import com.healthcare.patient.dto.response.HealthMetricResponse;
import com.healthcare.patient.dto.response.PatientResponse;

import java.time.LocalDate;
import java.util.List;

public interface PatientService {

     PatientResponse registerPatient(PatientRequest request);

     PatientResponse getPatient(Long id);

    void addHealthMetric(Long patientId, HealthMetricRequest request);

    List<HealthMetricResponse> getHealthMetrics(Long patientId);
}

package com.healthcare.patient.service;

import com.healthcare.patient.dto.request.PatientRequest;
import com.healthcare.patient.dto.response.PatientResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface PatientService {

     PatientResponse registerPatient(PatientRequest request);

     PatientResponse getPatient(Long id);
}

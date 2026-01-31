package com.healthcare.patient.repository;

import com.healthcare.patient.entity.HealthMetric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HealthMetricRepository extends JpaRepository<HealthMetric, Long> {

    List<HealthMetric> findByPatientIdOrderByMeasuredAtDesc(Long patientId);
}


package com.healthcare.patient.dto.request;

import com.healthcare.patient.constant.PatientConstants;
import jakarta.validation.constraints.NotBlank;

public record HealthMetricRequest(
        @NotBlank(message = PatientConstants.VAL_METRIC_TYPE_REQUIRED)
        String metricType, // "WEIGHT", "BP"

        @NotBlank(message = PatientConstants.VAL_METRIC_VALUE_REQUIRED)
        String value,      // "75.5"

        @NotBlank(message = PatientConstants.VAL_METRIC_UNIT_REQUIRED)
        String unit        // "kg"
)
{
}

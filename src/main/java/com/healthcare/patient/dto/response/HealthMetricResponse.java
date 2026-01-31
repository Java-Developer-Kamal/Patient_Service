package com.healthcare.patient.dto.response;

import java.time.LocalDateTime;

public record HealthMetricResponse(
        Long id,
        String metricType,
        String value,
        String unit,
        LocalDateTime measuredAt
) {
}

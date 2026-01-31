package com.healthcare.patient.mapper;

import ch.qos.logback.core.model.ComponentModel;
import com.healthcare.patient.dto.request.HealthMetricRequest;
import com.healthcare.patient.dto.response.HealthMetricResponse;
import com.healthcare.patient.entity.HealthMetric;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HealthMetricMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patientId", ignore = true) // Set manually in service
    @Mapping(target = "measuredAt", ignore = true) // Handled by Auditing
    HealthMetric toEntity(HealthMetricRequest request);

    HealthMetricResponse toResponse(HealthMetric entity);
}

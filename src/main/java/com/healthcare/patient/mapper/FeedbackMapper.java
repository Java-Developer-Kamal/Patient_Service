package com.healthcare.patient.mapper;

import com.healthcare.patient.dto.request.FeedbackRequest;
import com.healthcare.patient.dto.response.FeedbackResponse;
import com.healthcare.patient.entity.Feedback;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "patient", ignore = true)
    Feedback toEntity(FeedbackRequest request);

    FeedbackResponse toResponse(Feedback feedback);
}

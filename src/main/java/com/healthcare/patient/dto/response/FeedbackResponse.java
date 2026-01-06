package com.healthcare.patient.dto.response;

import java.time.LocalDateTime;

public record FeedbackResponse(
        Long id,
        Integer rating,
        String comments,
        LocalDateTime createdAt
) {}
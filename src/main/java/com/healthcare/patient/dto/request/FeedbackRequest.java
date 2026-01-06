package com.healthcare.patient.dto.request;

import com.healthcare.patient.constant.FeedbackConstants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FeedbackRequest(
        @NotNull(message = FeedbackConstants.MSG_RATING_REQUIRED)
        @Min(value = 1, message = FeedbackConstants.MSG_RATING_MIN)
        @Max(value = 5, message = FeedbackConstants.MSG_RATING_MAX)
        Integer rating,

        @Size(max = 1000, message = FeedbackConstants.MSG_COMMENTS_SIZE)
        String comments
) {}
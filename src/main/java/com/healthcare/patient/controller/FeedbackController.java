package com.healthcare.patient.controller;


import com.healthcare.patient.constant.FeedbackConstants;
import com.healthcare.patient.util.ApiResponse;
import com.healthcare.patient.dto.request.FeedbackRequest;
import com.healthcare.patient.dto.response.FeedbackResponse;
import com.healthcare.patient.service.impl.FeedbackServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patients/{patientId}/feedback")
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.tags.Tag(
        name = "Feedback Management",
        description = "Endpoints for submitting and viewing patient satisfaction surveys."
)
public class FeedbackController {

    private final FeedbackServiceImpl feedbackService;

    @io.swagger.v3.oas.annotations.Operation(
            summary = "Submit Patient Feedback",
            description = "Allows a registered patient to submit a satisfaction rating (1-5) and optional comments."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Feedback submitted successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validation Error (e.g., Rating < 1 or > 5)",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Patient ID not found",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiResponse<FeedbackResponse>> submitFeedback(
            @PathVariable Long patientId,
            @Valid @RequestBody FeedbackRequest request) {

        FeedbackResponse savedFeedback = feedbackService.submitFeedback(patientId, request);

        return new ResponseEntity<>(
                ApiResponse.success(savedFeedback, FeedbackConstants.SUCCESS_SUBMIT),
                HttpStatus.CREATED
        );
    }

    @io.swagger.v3.oas.annotations.Operation(
            summary = "View Feedback History",
            description = "Retrieves a paginated list of all feedback submitted by a specific patient."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "History retrieved successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Patient ID not found",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Page<FeedbackResponse>>> getPatientFeedback(
            @PathVariable Long patientId,
            Pageable pageable) {

        Page<FeedbackResponse> feedbackPage = feedbackService.getFeedbackByPatient(patientId, pageable);

        return ResponseEntity.ok(
                ApiResponse.success(feedbackPage, FeedbackConstants.SUCCESS_HISTORY)
        );
    }
}

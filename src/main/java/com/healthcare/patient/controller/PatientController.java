package com.healthcare.patient.controller;

import com.healthcare.patient.constant.PatientConstants;
import com.healthcare.patient.dto.request.HealthMetricRequest;
import com.healthcare.patient.util.ApiResponse;
import com.healthcare.patient.dto.request.PatientRequest;
import com.healthcare.patient.dto.response.PatientResponse;
import com.healthcare.patient.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EntityListeners;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
@Tag(name = "Patient Management", description = "APIs for managing patient profiles and emergency contacts")
@EntityListeners(AuditingEntityListener.class)
public class PatientController {

    private final PatientService service;

    @Operation(
            summary = "Register a new patient",
            description = "Creates a new patient account record with personal details and emergency contacts. Validates uniqueness of Email and Phone."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Patient created successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validation Error (e.g., Invalid Email, Future DOB)",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Conflict - Email or Phone already exists",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiResponse<PatientResponse>> registerPatient(
            @Valid @RequestBody PatientRequest request) {
        PatientResponse savedPatient = service.registerPatient(request);
        ApiResponse<PatientResponse> response = ApiResponse.success(
                savedPatient, PatientConstants.SUCCESS_REGISTER
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get Patient Profile",
            description = "Retrieves the full profile of a patient by their unique database ID."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Profile retrieved successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Patient not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiResponse<PatientResponse>> getPatient(
            @PathVariable Long id) {
        PatientResponse patient = service.getPatient(id);
        ApiResponse<PatientResponse> response = ApiResponse.success(
                patient, PatientConstants.SUCCESS_RETRIEVE
        );
        return ResponseEntity.ok(response);
    }

    // ========================================================================
    // USE CASE 7: HEALTH MONITORING
    // ========================================================================

        @Operation(summary = "Add Health Metric (Use Case 7)")
        @PostMapping("/{id}/health-metrics")
        public ResponseEntity<ApiResponse<Void>> addHealthMetric(
                @PathVariable Long id,
                @Valid @RequestBody HealthMetricRequest request) {

            service.addHealthMetric(id, request);

            return new ResponseEntity<>(
                    ApiResponse.success(null, PatientConstants.MSG_METRIC_ADDED),
                    HttpStatus.CREATED
            );
        }

    @Operation(summary = "View Health Metrics History (Use Case 7)")
    @GetMapping("/{id}/health-metrics")
    public ResponseEntity<ApiResponse<List<Object>>> getHealthMetrics(@PathVariable Long id) {
        // Casting List<HealthMetricResponse> to List<Object> for generic ApiResponse
        List<Object> metrics = new ArrayList<>(service.getHealthMetrics(id));

        return ResponseEntity.ok(
                ApiResponse.success(metrics, PatientConstants.MSG_METRICS_RETRIEVED)
        );
    }
}
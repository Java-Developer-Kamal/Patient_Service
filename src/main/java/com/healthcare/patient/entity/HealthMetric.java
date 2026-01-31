package com.healthcare.patient.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "health_metrics")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class HealthMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long patientId; // Linking to Patient

    @Column(nullable = false)
    private String metricType; // e.g., "WEIGHT", "BP", "GLUCOSE"

    @Column(nullable = false)
    private String value; // e.g., "75", "120/80", "95"

    @Column(nullable = false)
    private String unit; // e.g., "kg", "mmHg", "mg/dL"

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime measuredAt; // When was this recorded?
}

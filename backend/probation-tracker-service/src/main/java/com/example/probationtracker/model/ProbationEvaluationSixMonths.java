package com.example.probationtracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "probation_evaluations_six")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProbationEvaluationSixMonths {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "emp_code", referencedColumnName = "emp_code", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "evaluator_emp_code", referencedColumnName = "emp_code", nullable = false)
    private Employee evaluator;

    @Column(name = "evaluation_type", nullable = false)
    private String evaluationType; // e.g., "THIRD_MONTH", "SIXTH_MONTH"

    @Column(name = "evaluation_date", nullable = false)
    private LocalDate evaluationDate;

    // Individual feedback fields for each criteria
    @Column(name = "performance_standard_feedback", columnDefinition = "TEXT")
    private String performanceStandardFeedback;

    @Column(name = "quality_of_work_feedback", columnDefinition = "TEXT")
    private String qualityOfWorkFeedback;

    @Column(name = "subject_knowledge_competence_level_feedback", columnDefinition = "TEXT")
    private String subjectKnowledgeCompetenceLevelFeedback;

    @Column(name = "initiative_willingness_to_take_responsibilities_feedback", columnDefinition = "TEXT")
    private String initiativeWillingnessToTakeResponsibilitiesFeedback;

    @Column(name = "attendance_consistency_in_work_feedback", columnDefinition = "TEXT")
    private String attendanceConsistencyInWorkFeedback;

    @Column(name = "team_work_cooperation_feedback", columnDefinition = "TEXT")
    private String teamWorkCooperationFeedback;

    @Column(name = "organizing_time_management_feedback", columnDefinition = "TEXT")
    private String organizingTimeManagementFeedback;

    @Column(name = "attitude_towards_work_feedback", columnDefinition = "TEXT")
    private String attitudeTowardsWorkFeedback;

    @Column(name = "well_versed_with_company_policies_feedback", columnDefinition = "TEXT")
    private String wellVersedWithCompanyPoliciesFeedback;

    @Column(name = "thorough_with_company_code_of_conduct_feedback", columnDefinition = "TEXT")
    private String thoroughWithCompanyCodeOfConductFeedback;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

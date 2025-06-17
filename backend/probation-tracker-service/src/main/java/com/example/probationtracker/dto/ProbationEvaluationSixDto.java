package com.example.probationtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProbationEvaluationSixDto {
    private Long id;
    private String empCode;
    private String evaluationType;
    private LocalDate evaluationDate;
    private String evaluatorName;

    // Individual feedback fields
    private String performanceStandardFeedback;
    private String qualityOfWorkFeedback;
    private String subjectKnowledgeCompetenceLevelFeedback;
    private String initiativeWillingnessToTakeResponsibilitiesFeedback;
    private String attendanceConsistencyInWorkFeedback;
    private String teamWorkCooperationFeedback;
    private String organizingTimeManagementFeedback;
    private String attitudeTowardsWorkFeedback;
    private String wellVersedWithCompanyPoliciesFeedback;
    private String thoroughWithCompanyCodeOfConductFeedback;
    private String remarks;
}

package com.example.probationtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProbationRecordDTO {
    private Long id;

    private String empCode; // Extracted from Employee object
    private String employeeName; // Optional - for display purposes

    private Long probationEvaluationId; // ID of third-month evaluation
    private Long probationEvaluationSixMonthsId; // ID of sixth-month evaluation

    private LocalDate actualProbationEndDate;
    private LocalDate extendedDate;
    private LocalDate terminationDate;
    private int totalNumberExtended;
    private Integer probationDays;
    private LocalDate currentProbationEndDate;

    private String r1ApprovalStatus;
    private String hrStatus;
    private String status;
    private String managerEmpCode;
    private String hrEmpCode;
    private String comments;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

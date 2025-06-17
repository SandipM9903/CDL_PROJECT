package com.example.probationtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeFullDetailsDTO {
    // Employee Details
    private Long empId;
    private String empCode;
    private String profilePicUrl;
    private String firstName;
    private String lastName;
    private String emailId;
    private String roles;
    private String primaryContactNo;
    private String reportingManager;
    private String reportingManagerEmailId;
    private LocalDate dateOfJoining;
    private String projectCostCentre;
    private String location;
    private Integer probationDay;
    private String employeeStatus; // Renamed to avoid clash with probation status in some contexts

    // Probation Record Details (only what's left after moving status/probationDay to Employee)
    private LocalDate actualProbationEndDate;
    private LocalDate currentProbationEndDate;
    private String r1ApprovalStatus;
    private String hrStatus;

    // Derived/Calculated fields for frontend
    private Integer probationExtendedNoOfTimes;
    private Integer confirmationOverdueDays;

    // Timeline/Action Log Details
    private List<ProbationActionLogDTO> actionLogs;

    // Evaluation Details (for 3rd and 6th month)
    private List<ProbationEvaluationDTO> evaluations;
}
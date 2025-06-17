package com.example.probationtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeProbationSummaryDTO {
    private Long id;
    private String empCode;
    private String profilePic;
    private String name;
    private String email;
    private String role;
    private String phoneNumber;
    private String r1ApprovalStatus;
    private String hrStatus;
    private String department;
    private String rsManager;
    private LocalDate dateOfJoining;
    private Integer probationDays; // Now sourced from Employee entity
    private LocalDate actualProbationEndDate;
    private Integer probationExtendedNoOfTimes;
    private String status; // Current probation status (now sourced from Employee entity)
    private Integer confirmationOverdueDays;
    private LocalDate currentProbationEndDate;
}
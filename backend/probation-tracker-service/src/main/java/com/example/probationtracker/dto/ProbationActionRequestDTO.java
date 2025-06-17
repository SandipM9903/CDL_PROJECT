package com.example.probationtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProbationActionRequestDTO {
    private String empCode;
    private String actionType; // e.g., "CONFIRM", "EXTENSION", "TERMINATION"
    private LocalDate actionDate;
    private LocalDate newProbationEndDate; // Used for EXTENSION, or termination date for TERMINATION
    private Integer extensionDays; // Used for EXTENSION (alternative to newProbationEndDate)
    private String comments;
    private String recordedByEmpCode; // EmpCode of the person logging the action (e.g., HR, Manager)
}
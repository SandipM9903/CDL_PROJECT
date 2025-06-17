package com.example.probationtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProbationActionLogDTO {
    private Long id;
    private String empCode;
    private String actionType;
    private LocalDate actionDate;
    private LocalDate newProbationEndDate;
    private Integer extensionDays;
    private String comments;
    private String recordedByName; // Full name of the employee who recorded the action
}
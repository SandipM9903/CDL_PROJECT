package com.example.probationtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProbationEvaluationRequestDTO {
    private String empCode;
    private String evaluatorEmpCode;
    private LocalDate evaluationDate;
    private Map<Integer, String> thirdMonthFeedback;
    private String thirdMonthRemarks;
    private Map<Integer, String> sixthMonthFeedback;
    private String sixthMonthRemarks;
}
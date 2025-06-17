package com.example.probationtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private Long empId;
    private String empCode;
    private String profilePicUrl;
    private String firstName;
    private String lastName;
    private String emailId;
    private String roles;
    private String primaryContactNo;
    private String reportingManager;
    private String hiringHr;
    private String reportingManagerEmailId;
    private LocalDate dateOfJoining;
    private String projectCostCentre;
    private String location;
    private Integer probationDay;
    private String status;
}
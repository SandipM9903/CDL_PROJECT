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
@Table(name = "employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private Long empId;

    @Column(name = "emp_code", unique = true, nullable = false)
    private String empCode;

    @Column(name = "profile_pic_url")
    private String profilePicUrl;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email_id", unique = true, nullable = false)
    private String emailId;

    @Column(name = "roles")
    private String roles;

    @Column(name = "primary_contact_no")
    private String primaryContactNo;

    @Column(name = "reporting_manager")
    private String reportingManager;

    @Column(name = "hiring_hr")
    private String hiringHr;

    @Column(name = "reporting_manager_email_id")
    private String reportingManagerEmailId;

    @Column(name = "date_of_joining", nullable = false)
    private LocalDate dateOfJoining;

    @Column(name = "project_cost_centre")
    private String projectCostCentre;

    @Column(name = "location")
    private String location;

    @Column(name = "probation_day")
    private Integer probationDay;

    @Column(name = "status")
    private String status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
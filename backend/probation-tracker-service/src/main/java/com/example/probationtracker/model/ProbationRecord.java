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
@Table(name = "probation_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProbationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "emp_code", referencedColumnName = "emp_code", nullable = false)
    private Employee employee;

    @OneToOne
    @JoinColumn(name = "probation_evaluation_id", referencedColumnName = "id")
    private ProbationEvaluation probationEvaluation;

    @Column(name = "probation_days", nullable = false)
    private int probationDays;

    @OneToOne
    @JoinColumn(name = "probation_evaluation_overall_id", referencedColumnName = "id")
    private ProbationEvaluationSixMonths probationEvaluationSixMonths;

    @Column(name = "manager_emp_code")
    private String managerEmpCode;

    @Column(name = "hr_emp_code")
    private String hrEmpCode;

    @Column(name = "actual_probation_end_date", nullable = false)
    private LocalDate actualProbationEndDate;

    @Column(name = "extended_date")
    private LocalDate extendedDate;

    @Column(name = "termination_date")
    private LocalDate terminationDate;

    @Column(name = "total_extended_times", nullable = false)
    private int totalNumberExtended;

    @Column(name = "current_probation_end_date", nullable = false)
    private LocalDate currentProbationEndDate;

    @Column(name = "status")
    private String status;

    @Column(name = "r1_approval_status")
    private String r1ApprovalStatus;

    @Column(name = "hr_status")
    private String hrStatus;

    @Column(name = "comments")
    private String comments;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
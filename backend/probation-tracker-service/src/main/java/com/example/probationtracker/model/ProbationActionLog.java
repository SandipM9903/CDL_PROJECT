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
@Table(name = "probation_action_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProbationActionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "emp_code", referencedColumnName = "emp_code", nullable = false)
    private Employee employee;

    @Column(name = "action_type", nullable = false)
    private String actionType; // e.g., "JOINING", "EXTENSION", "CONFIRM", "TERMINATION"

    @Column(name = "action_date", nullable = false)
    private LocalDate actionDate;

    @Column(name = "new_probation_end_date")
    private LocalDate newProbationEndDate; // Relevant for EXTENSION, or termination date for TERMINATION

    @Column(name = "extension_days")
    private Integer extensionDays; // Relevant for EXTENSION

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @ManyToOne
    @JoinColumn(name = "recorded_by_emp_code", referencedColumnName = "emp_code")
    private Employee recordedBy; // Employee who recorded the action (e.g., HR, Manager)

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
package com.example.probationtracker.repository;

import com.example.probationtracker.model.Employee;
import com.example.probationtracker.model.ProbationEvaluation;
import com.example.probationtracker.model.ProbationEvaluationSixMonths;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProbationEvaluationSixRepository extends JpaRepository<ProbationEvaluationSixMonths, Long> {
    List<ProbationEvaluationSixMonths> findByEmployeeOrderByEvaluationDateDesc(Employee employee);
    Optional<ProbationEvaluationSixMonths> findByIdAndEmployee_EmpCode(Long id, String empCode);

    List<ProbationEvaluationSixMonths> findByEmployeeOrderByCreatedAtDesc(Employee employee);

}
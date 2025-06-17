package com.example.probationtracker.repository;

import com.example.probationtracker.model.Employee;
import com.example.probationtracker.model.ProbationEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProbationEvaluationRepository extends JpaRepository<ProbationEvaluation, Long> {
    List<ProbationEvaluation> findByEmployeeOrderByEvaluationDateDesc(Employee employee);
    List<ProbationEvaluation> findByEmployee_EmpCode(String empCode);
    Optional<ProbationEvaluation> findByIdAndEmployee_EmpCode(Long id, String empCode);

    List<ProbationEvaluation> findByEmployeeOrderByCreatedAtDesc(Employee employee);
}
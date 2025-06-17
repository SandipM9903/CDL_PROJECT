package com.example.probationtracker.repository;

import com.example.probationtracker.model.Employee;
import com.example.probationtracker.model.ProbationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProbationRecordRepository extends JpaRepository<ProbationRecord, Long> {
    Optional<ProbationRecord> findByEmployee(Employee employee);
    Optional<ProbationRecord> findTopByEmployee_EmpCodeOrderByCreatedAtDesc(String empCode);
    List<ProbationRecord> findByEmployee_EmpCodeOrderByCreatedAtAsc(String empCode);
}
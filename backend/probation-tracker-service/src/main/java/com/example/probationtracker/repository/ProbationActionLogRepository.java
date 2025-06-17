package com.example.probationtracker.repository;

import com.example.probationtracker.model.Employee;
import com.example.probationtracker.model.ProbationActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProbationActionLogRepository extends JpaRepository<ProbationActionLog, Long> {
    List<ProbationActionLog> findByEmployeeOrderByActionDateDesc(Employee employee);
    long countByEmployeeAndActionType(Employee employee, String actionType);
}
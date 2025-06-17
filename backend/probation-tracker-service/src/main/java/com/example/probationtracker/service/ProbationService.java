package com.example.probationtracker.service;

import com.example.probationtracker.dto.*;
import com.example.probationtracker.model.*;
import com.example.probationtracker.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProbationService {

    private final EmployeeRepository employeeRepository;
    private final ProbationRecordRepository probationRecordRepository; // Still needed for other methods
    private final ProbationEvaluationRepository probationEvaluationRepository;
    private final ProbationEvaluationSixRepository probationEvaluationSixRepository;
    private final ProbationActionLogRepository probationActionLogRepository;

    @Autowired
    public ProbationService(EmployeeRepository employeeRepository,
                            ProbationRecordRepository probationRecordRepository,
                            ProbationEvaluationRepository probationEvaluationRepository,
                            ProbationEvaluationSixRepository probationEvaluationSixRepository,
                            ProbationActionLogRepository probationActionLogRepository) {
        this.employeeRepository = employeeRepository;
        this.probationRecordRepository = probationRecordRepository;
        this.probationEvaluationRepository = probationEvaluationRepository;
        this.probationEvaluationSixRepository = probationEvaluationSixRepository;
        this.probationActionLogRepository = probationActionLogRepository;
    }

    // --- Dashboard Summary ---
    @Transactional(readOnly = true)
    public List<EmployeeProbationSummaryDTO> getAllEmployeesProbationSummary(String filterStatus) {
        List<ProbationRecord> probationRecords;

        // Fetch all employees and then filter based on their status
        List<Employee> employees = employeeRepository.findAll();
        List<Employee> filteredEmployees = employees.stream()
                .filter(employee -> {
                    if ("All".equalsIgnoreCase(filterStatus)) {
                        return true;
                    } else if (employee.getStatus() != null) {
                        return employee.getStatus().equalsIgnoreCase(filterStatus);
                    }
                    return false; // Employee has no status or it doesn't match
                })
                .collect(Collectors.toList());

        // Now, find probation records for these filtered employees
        probationRecords = probationRecordRepository.findAll().stream()
                .filter(pr -> filteredEmployees.contains(pr.getEmployee()))
                .collect(Collectors.toList());

        return probationRecords.stream()
                .map(this::convertToEmployeeProbationSummaryDTO)
                .collect(Collectors.toList());
    }


    private EmployeeProbationSummaryDTO convertToEmployeeProbationSummaryDTO(ProbationRecord probationRecord) {
        Employee employee = probationRecord.getEmployee();
        if (employee == null) {
            throw new EntityNotFoundException("Employee not found for probation record ID: " + probationRecord.getId());
        }

        EmployeeProbationSummaryDTO dto = new EmployeeProbationSummaryDTO();
        dto.setId(employee.getEmpId());
        dto.setEmpCode(employee.getEmpCode());
        dto.setProfilePic(employee.getProfilePicUrl());
        dto.setName(employee.getFirstName() + " " + employee.getLastName());
        dto.setEmail(employee.getEmailId());
        dto.setRole(employee.getRoles());
        dto.setPhoneNumber(employee.getPrimaryContactNo());
        // Assuming department is a field in Employee entity
        dto.setDepartment(employee.getProjectCostCentre()); // Using projectCostCentre as a proxy for department
        dto.setRsManager(employee.getReportingManager());
        dto.setDateOfJoining(employee.getDateOfJoining());

        // Probation specific details - now primarily from Employee entity
        dto.setProbationDays(employee.getProbationDay());
        dto.setStatus(employee.getStatus());

        // These dates should ideally be derived from employee.getDateOfJoining and employee.getProbationDay if ProbationRecord is not used.
        // For summary, they are still taken from ProbationRecord. Keep this method as is for now.
        dto.setActualProbationEndDate(probationRecord.getActualProbationEndDate());
        dto.setCurrentProbationEndDate(probationRecord.getCurrentProbationEndDate());
        dto.setR1ApprovalStatus(probationRecord.getR1ApprovalStatus());
        dto.setHrStatus(probationRecord.getHrStatus());

        // Calculate probationExtendedNoOfTimes from action log
        long extensions = probationActionLogRepository.countByEmployeeAndActionType(employee, "EXTENSION");
        dto.setProbationExtendedNoOfTimes((int) extensions);

        // Calculate confirmationOverdueDays
        LocalDate today = LocalDate.now();
        if (probationRecord.getCurrentProbationEndDate().isBefore(today)) {
            dto.setConfirmationOverdueDays((int) ChronoUnit.DAYS.between(probationRecord.getCurrentProbationEndDate(), today));
        } else {
            dto.setConfirmationOverdueDays(0);
        }

        return dto;
    }

    // --- Employee Full Details ---
    @Transactional(readOnly = true)
    public EmployeeFullDetailsDTO getEmployeeFullDetails(String empCode) {
        Employee employee = employeeRepository.findByEmpCode(empCode)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with empCode: " + empCode));

        // REMOVED: ProbationRecord probationRecord = probationRecordRepository.findByEmployee(employee).orElseThrow(...)

        EmployeeFullDetailsDTO dto = new EmployeeFullDetailsDTO();

        // Employee Details - ALL FROM EMPLOYEE ENTITY
        dto.setEmpId(employee.getEmpId());
        dto.setEmpCode(employee.getEmpCode());
        dto.setProfilePicUrl(employee.getProfilePicUrl());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmailId(employee.getEmailId());
        dto.setRoles(employee.getRoles());
        dto.setPrimaryContactNo(employee.getPrimaryContactNo());
        dto.setReportingManager(employee.getReportingManager());
        dto.setReportingManagerEmailId(employee.getReportingManagerEmailId());
        dto.setDateOfJoining(employee.getDateOfJoining());
        dto.setProjectCostCentre(employee.getProjectCostCentre());
        dto.setLocation(employee.getLocation());
        dto.setProbationDay(employee.getProbationDay());
        dto.setEmployeeStatus(employee.getStatus());

        // Probation Related Dates - DERIVED FROM EMPLOYEE ONLY
        LocalDate actualProbationEndDate = employee.getDateOfJoining().plusDays(employee.getProbationDay());
        dto.setActualProbationEndDate(actualProbationEndDate);
        // Assuming currentProbationEndDate is the same as actual if no extensions are tracked in Employee
        // or it's the result of latest extension. For strict "Employee only", it's initial.
        dto.setCurrentProbationEndDate(actualProbationEndDate); // Set to initial probation end date

        // R1ApprovalStatus and HrStatus - REMOVED as they are not in Employee entity
        // If you need these, you must add them to your Employee entity.

        // Calculate probationExtendedNoOfTimes - Still uses employee to query action logs
        long extensions = probationActionLogRepository.countByEmployeeAndActionType(employee, "EXTENSION");
        dto.setProbationExtendedNoOfTimes((int) extensions);

        // Calculate confirmationOverdueDays - now uses derived currentProbationEndDate
        LocalDate today = LocalDate.now();
        if (dto.getCurrentProbationEndDate().isBefore(today)) { // Use the derived date
            dto.setConfirmationOverdueDays((int) ChronoUnit.DAYS.between(dto.getCurrentProbationEndDate(), today));
        } else {
            dto.setConfirmationOverdueDays(0);
        }

        // Action Logs (Timeline) - Still uses employee, so this is fine
        List<ProbationActionLog> actionLogs = probationActionLogRepository.findByEmployeeOrderByActionDateDesc(employee);
        List<ProbationActionLogDTO> actionLogDTOs = actionLogs.stream()
                .map(log -> {
                    ProbationActionLogDTO logDto = new ProbationActionLogDTO();
                    logDto.setId(log.getId());
                    logDto.setEmpCode(log.getEmployee().getEmpCode());
                    logDto.setActionType(log.getActionType());
                    logDto.setActionDate(log.getActionDate());
                    logDto.setNewProbationEndDate(log.getNewProbationEndDate());
                    logDto.setExtensionDays(log.getExtensionDays());
                    logDto.setComments(log.getComments());
                    Optional.ofNullable(log.getRecordedBy())
                            .ifPresent(recorder -> logDto.setRecordedByName(recorder.getFirstName() + " " + recorder.getLastName()));
                    return logDto;
                })
                .collect(Collectors.toList());
        dto.setActionLogs(actionLogDTOs);

        // Evaluations - Still uses employee, so this is fine
        List<ProbationEvaluation> evaluations = probationEvaluationRepository.findByEmployeeOrderByEvaluationDateDesc(employee);
        List<ProbationEvaluationDTO> evaluationDTOs = evaluations.stream()
                .map(eval -> {
                    ProbationEvaluationDTO evalDto = new ProbationEvaluationDTO();
                    evalDto.setId(eval.getId());
                    evalDto.setEmpCode(eval.getEmployee().getEmpCode());
                    evalDto.setEvaluationType(eval.getEvaluationType());
                    evalDto.setEvaluationDate(eval.getEvaluationDate());
                    Optional.ofNullable(eval.getEvaluator())
                            .ifPresent(evaluator -> evalDto.setEvaluatorName(evaluator.getFirstName() + " " + evaluator.getLastName()));

                    evalDto.setPerformanceStandardFeedback(eval.getPerformanceStandardFeedback());
                    evalDto.setQualityOfWorkFeedback(eval.getQualityOfWorkFeedback());
                    evalDto.setSubjectKnowledgeCompetenceLevelFeedback(eval.getSubjectKnowledgeCompetenceLevelFeedback());
                    evalDto.setInitiativeWillingnessToTakeResponsibilitiesFeedback(eval.getInitiativeWillingnessToTakeResponsibilitiesFeedback());
                    evalDto.setAttendanceConsistencyInWorkFeedback(eval.getAttendanceConsistencyInWorkFeedback());
                    evalDto.setTeamWorkCooperationFeedback(eval.getTeamWorkCooperationFeedback());
                    evalDto.setOrganizingTimeManagementFeedback(eval.getOrganizingTimeManagementFeedback());
                    evalDto.setAttitudeTowardsWorkFeedback(eval.getAttitudeTowardsWorkFeedback());
                    evalDto.setWellVersedWithCompanyPoliciesFeedback(eval.getWellVersedWithCompanyPoliciesFeedback());
                    evalDto.setThoroughWithCompanyCodeOfConductFeedback(eval.getThoroughWithCompanyCodeOfConductFeedback());
                    evalDto.setRemarks(eval.getRemarks());
                    return evalDto;
                })
                .collect(Collectors.toList());
        dto.setEvaluations(evaluationDTOs);

        return dto;
    }

    // --- Handle Probation Evaluation Submission ---
    @Transactional
    public void submitProbationEvaluation(ProbationEvaluationRequestDTO requestDTO) {
        Employee employee = employeeRepository.findByEmpCode(requestDTO.getEmpCode())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with empCode: " + requestDTO.getEmpCode()));

        Employee evaluator = employeeRepository.findByEmpCode(requestDTO.getEvaluatorEmpCode())
                .orElseThrow(() -> new EntityNotFoundException("Evaluator employee not found with empCode: " + requestDTO.getEvaluatorEmpCode()));

        // Save Third Month Evaluation
        ProbationEvaluation thirdMonthEval = new ProbationEvaluation();
        thirdMonthEval.setEmployee(employee);
        thirdMonthEval.setEvaluationType("THIRD_MONTH");
        thirdMonthEval.setEvaluationDate(requestDTO.getEvaluationDate());
        thirdMonthEval.setEvaluator(evaluator);
        thirdMonthEval.setPerformanceStandardFeedback(requestDTO.getThirdMonthFeedback().getOrDefault(0, ""));
        thirdMonthEval.setQualityOfWorkFeedback(requestDTO.getThirdMonthFeedback().getOrDefault(1, ""));
        thirdMonthEval.setSubjectKnowledgeCompetenceLevelFeedback(requestDTO.getThirdMonthFeedback().getOrDefault(2, ""));
        thirdMonthEval.setInitiativeWillingnessToTakeResponsibilitiesFeedback(requestDTO.getThirdMonthFeedback().getOrDefault(3, ""));
        thirdMonthEval.setAttendanceConsistencyInWorkFeedback(requestDTO.getThirdMonthFeedback().getOrDefault(4, ""));
        thirdMonthEval.setTeamWorkCooperationFeedback(requestDTO.getThirdMonthFeedback().getOrDefault(5, ""));
        thirdMonthEval.setOrganizingTimeManagementFeedback(requestDTO.getThirdMonthFeedback().getOrDefault(6, ""));
        thirdMonthEval.setAttitudeTowardsWorkFeedback(requestDTO.getThirdMonthFeedback().getOrDefault(7, ""));
        thirdMonthEval.setWellVersedWithCompanyPoliciesFeedback(requestDTO.getThirdMonthFeedback().getOrDefault(8, ""));
        thirdMonthEval.setThoroughWithCompanyCodeOfConductFeedback(requestDTO.getThirdMonthFeedback().getOrDefault(9, ""));
        thirdMonthEval.setRemarks(requestDTO.getThirdMonthRemarks());
        probationEvaluationRepository.save(thirdMonthEval);

        // Save Sixth Month Evaluation (Employment Confirmation Evaluation)
        ProbationEvaluation sixthMonthEval = new ProbationEvaluation();
        sixthMonthEval.setEmployee(employee);
        sixthMonthEval.setEvaluationType("SIXTH_MONTH");
        sixthMonthEval.setEvaluationDate(requestDTO.getEvaluationDate());
        sixthMonthEval.setEvaluator(evaluator);
        sixthMonthEval.setPerformanceStandardFeedback(requestDTO.getSixthMonthFeedback().getOrDefault(0, ""));
        sixthMonthEval.setQualityOfWorkFeedback(requestDTO.getSixthMonthFeedback().getOrDefault(1, ""));
        sixthMonthEval.setSubjectKnowledgeCompetenceLevelFeedback(requestDTO.getSixthMonthFeedback().getOrDefault(2, ""));
        sixthMonthEval.setInitiativeWillingnessToTakeResponsibilitiesFeedback(requestDTO.getSixthMonthFeedback().getOrDefault(3, ""));
        sixthMonthEval.setAttendanceConsistencyInWorkFeedback(requestDTO.getSixthMonthFeedback().getOrDefault(4, ""));
        sixthMonthEval.setTeamWorkCooperationFeedback(requestDTO.getSixthMonthFeedback().getOrDefault(5, ""));
        sixthMonthEval.setOrganizingTimeManagementFeedback(requestDTO.getSixthMonthFeedback().getOrDefault(6, ""));
        sixthMonthEval.setAttitudeTowardsWorkFeedback(requestDTO.getSixthMonthFeedback().getOrDefault(7, ""));
        sixthMonthEval.setWellVersedWithCompanyPoliciesFeedback(requestDTO.getSixthMonthFeedback().getOrDefault(8, ""));
        sixthMonthEval.setThoroughWithCompanyCodeOfConductFeedback(requestDTO.getSixthMonthFeedback().getOrDefault(9, ""));
        sixthMonthEval.setRemarks(requestDTO.getSixthMonthRemarks());
        probationEvaluationRepository.save(sixthMonthEval);
    }

    // --- Handle Probation Action (Confirm, Extend, Terminate) ---
    @Transactional
    public void handleProbationAction(ProbationActionRequestDTO requestDTO) {
        Employee employee = employeeRepository.findByEmpCode(requestDTO.getEmpCode())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with empCode: " + requestDTO.getEmpCode()));

        // Keep ProbationRecord here as these actions (extension/termination) directly modify it
        ProbationRecord probationRecord = probationRecordRepository.findByEmployee(employee)
                .orElseThrow(() -> new EntityNotFoundException("Probation record not found for employee: " + requestDTO.getEmpCode()));

        Employee recordedBy = null;
        if (requestDTO.getRecordedByEmpCode() != null) {
            recordedBy = employeeRepository.findByEmpCode(requestDTO.getRecordedByEmpCode())
                    .orElseThrow(() -> new EntityNotFoundException("Recorded by employee not found with empCode: " + requestDTO.getRecordedByEmpCode()));
        }

        // Create a log entry for the action
        ProbationActionLog actionLog = new ProbationActionLog();
        actionLog.setEmployee(employee);
        actionLog.setActionType(requestDTO.getActionType());
        actionLog.setActionDate(requestDTO.getActionDate() != null ? requestDTO.getActionDate() : LocalDate.now());
        actionLog.setComments(requestDTO.getComments());
        actionLog.setRecordedBy(recordedBy);

        switch (requestDTO.getActionType()) {
            case "CONFIRM":
                employee.setStatus("Confirmed"); // Update status in Employee entity
                probationRecord.setR1ApprovalStatus("Completed"); // Example, if R1 is related to confirmation
                probationRecord.setHrStatus("Completed"); // Example, if HR is related to confirmation
                break;
            case "EXTENSION":
                // If the employee's status needs to be directly modified here
                employee.setStatus("Probation Extended");
                if (requestDTO.getNewProbationEndDate() != null) {
                    probationRecord.setCurrentProbationEndDate(requestDTO.getNewProbationEndDate());
                    actionLog.setNewProbationEndDate(requestDTO.getNewProbationEndDate());
                    LocalDate previousEndDate = probationRecord.getCurrentProbationEndDate();
                    if (previousEndDate != null) {
                        actionLog.setExtensionDays((int) ChronoUnit.DAYS.between(previousEndDate, requestDTO.getNewProbationEndDate()));
                    }
                } else if (requestDTO.getExtensionDays() != null && requestDTO.getExtensionDays() > 0) {
                    LocalDate newEndDate = probationRecord.getCurrentProbationEndDate().plusDays(requestDTO.getExtensionDays());
                    probationRecord.setCurrentProbationEndDate(newEndDate);
                    actionLog.setNewProbationEndDate(newEndDate);
                    actionLog.setExtensionDays(requestDTO.getExtensionDays());
                } else {
                    throw new IllegalArgumentException("Extension action requires either new probation end date or extension days.");
                }
                break;
            case "TERMINATION":
                if (requestDTO.getNewProbationEndDate() == null) {
                    throw new IllegalArgumentException("Termination action requires a termination date.");
                }
                employee.setStatus("Terminated"); // Update status in Employee entity
                actionLog.setNewProbationEndDate(requestDTO.getNewProbationEndDate());
                break;
            default:
                throw new IllegalArgumentException("Invalid action type: " + requestDTO.getActionType());
        }
        employeeRepository.save(employee);
        probationRecordRepository.save(probationRecord); // Save probation record changes
        probationActionLogRepository.save(actionLog);
    }

    // --- Initial Data Population (for testing purposes only) ---
    @Transactional
    public void initializeProbationRecords() {
        List<Employee> employeesWithoutProbation = employeeRepository.findAll().stream()
                .filter(employee -> probationRecordRepository.findByEmployee(employee).isEmpty())
                .collect(Collectors.toList());

        for (Employee employee : employeesWithoutProbation) {
            // Set initial probation properties directly on Employee if you want Employee to be the single source
            // Or ensure these are consistent with the new ProbationRecord
            employee.setProbationDay(90); // Example, assuming all new employees start with 90 days
            employee.setStatus("Probation"); // Set initial status

            ProbationRecord newRecord = new ProbationRecord();
            newRecord.setEmployee(employee);
            // Calculate initial end dates based on Employee's dateOfJoining and probationDay
            newRecord.setActualProbationEndDate(employee.getDateOfJoining().plusDays(employee.getProbationDay()));
            newRecord.setCurrentProbationEndDate(employee.getDateOfJoining().plusDays(employee.getProbationDay()));
            newRecord.setR1ApprovalStatus("Pending"); // Default initial status
            newRecord.setHrStatus("Pending");       // Default initial status

            employeeRepository.save(employee); // Save updated employee status/probationDay
            probationRecordRepository.save(newRecord);

            ProbationActionLog initialLog = new ProbationActionLog();
            initialLog.setEmployee(employee);
            initialLog.setActionType("JOINING");
            initialLog.setActionDate(employee.getDateOfJoining());
            initialLog.setComments("Employee joined and started probation.");
            probationActionLogRepository.save(initialLog);
        }
    }

    // --- New API: Get All Employees with Filters ---
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAllEmployees(String searchTerm, String roleFilter, String statusFilter) {
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream()
                .filter(employee -> {
                    boolean matchesSearchTerm = true;
                    if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                        String lowerCaseSearchTerm = searchTerm.trim().toLowerCase();
                        matchesSearchTerm = (employee.getFirstName() != null && employee.getFirstName().toLowerCase().contains(lowerCaseSearchTerm)) ||
                                (employee.getLastName() != null && employee.getLastName().toLowerCase().contains(lowerCaseSearchTerm)) ||
                                (employee.getEmpCode() != null && employee.getEmpCode().toLowerCase().contains(lowerCaseSearchTerm)) ||
                                (employee.getEmailId() != null && employee.getEmailId().toLowerCase().contains(lowerCaseSearchTerm));
                    }

                    boolean matchesRole = true;
                    if (roleFilter != null && !roleFilter.trim().isEmpty()) {
                        matchesRole = (employee.getRoles() != null && employee.getRoles().equalsIgnoreCase(roleFilter.trim()));
                    }

                    boolean matchesStatus = true;
                    if (statusFilter != null && !statusFilter.trim().isEmpty()) {
                        if ("All".equalsIgnoreCase(statusFilter)) {
                            matchesStatus = true;
                        } else if (employee.getStatus() != null) {
                            matchesStatus = employee.getStatus().equalsIgnoreCase(statusFilter.trim());
                        } else {
                            matchesStatus = false;
                        }
                    }

                    return matchesSearchTerm && matchesRole && matchesStatus;
                })
                .map(employee -> {
                    EmployeeDTO dto = new EmployeeDTO();
                    dto.setEmpId(employee.getEmpId());
                    dto.setEmpCode(employee.getEmpCode());
                    dto.setProfilePicUrl(employee.getProfilePicUrl());
                    dto.setFirstName(employee.getFirstName());
                    dto.setLastName(employee.getLastName());
                    dto.setEmailId(employee.getEmailId());
                    dto.setRoles(employee.getRoles());
                    dto.setPrimaryContactNo(employee.getPrimaryContactNo());
                    dto.setReportingManager(employee.getReportingManager());
                    dto.setReportingManagerEmailId(employee.getReportingManagerEmailId());
                    dto.setDateOfJoining(employee.getDateOfJoining());
                    dto.setProjectCostCentre(employee.getProjectCostCentre());
                    dto.setLocation(employee.getLocation());
                    dto.setProbationDay(employee.getProbationDay());
                    dto.setStatus(employee.getStatus());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    //Needed

    @Transactional
    public ProbationEvaluation createProbationEvaluation(ProbationEvaluationDTO dto) {
        Employee employee = employeeRepository.findByEmpCode(dto.getEmpCode())
                .orElseThrow(() -> new RuntimeException("Employee not found with empCode: " + dto.getEmpCode()));

        Employee evaluator = employeeRepository.findByEmpCode(dto.getEvaluatorName())
                .orElseThrow(() -> new RuntimeException("Evaluator not found with empCode: " + dto.getEvaluatorName()));

        ProbationEvaluation evaluation = new ProbationEvaluation();

        evaluation.setEmployee(employee);
        evaluation.setEvaluator(evaluator);
        evaluation.setEvaluationType(dto.getEvaluationType());
        evaluation.setEvaluationDate(dto.getEvaluationDate());

        evaluation.setPerformanceStandardFeedback(dto.getPerformanceStandardFeedback());
        evaluation.setQualityOfWorkFeedback(dto.getQualityOfWorkFeedback());
        evaluation.setSubjectKnowledgeCompetenceLevelFeedback(dto.getSubjectKnowledgeCompetenceLevelFeedback());
        evaluation.setInitiativeWillingnessToTakeResponsibilitiesFeedback(dto.getInitiativeWillingnessToTakeResponsibilitiesFeedback());
        evaluation.setAttendanceConsistencyInWorkFeedback(dto.getAttendanceConsistencyInWorkFeedback());
        evaluation.setTeamWorkCooperationFeedback(dto.getTeamWorkCooperationFeedback());
        evaluation.setOrganizingTimeManagementFeedback(dto.getOrganizingTimeManagementFeedback());
        evaluation.setAttitudeTowardsWorkFeedback(dto.getAttitudeTowardsWorkFeedback());
        evaluation.setWellVersedWithCompanyPoliciesFeedback(dto.getWellVersedWithCompanyPoliciesFeedback());
        evaluation.setThoroughWithCompanyCodeOfConductFeedback(dto.getThoroughWithCompanyCodeOfConductFeedback());
        evaluation.setRemarks(dto.getRemarks());

        return probationEvaluationRepository.save(evaluation);
    }

    @Transactional
    public ProbationEvaluationSixMonths createProbationEvaluationSix(ProbationEvaluationSixDto dto) {
        Employee employee = employeeRepository.findByEmpCode(dto.getEmpCode())
                .orElseThrow(() -> new RuntimeException("Employee not found with empCode: " + dto.getEmpCode()));

        Employee evaluator = employeeRepository.findByEmpCode(dto.getEvaluatorName())
                .orElseThrow(() -> new RuntimeException("Evaluator not found with empCode: " + dto.getEvaluatorName()));

        ProbationEvaluationSixMonths evaluation = new ProbationEvaluationSixMonths();

        evaluation.setEmployee(employee);
        evaluation.setEvaluator(evaluator);
        evaluation.setEvaluationType(dto.getEvaluationType());
        evaluation.setEvaluationDate(dto.getEvaluationDate());

        evaluation.setPerformanceStandardFeedback(dto.getPerformanceStandardFeedback());
        evaluation.setQualityOfWorkFeedback(dto.getQualityOfWorkFeedback());
        evaluation.setSubjectKnowledgeCompetenceLevelFeedback(dto.getSubjectKnowledgeCompetenceLevelFeedback());
        evaluation.setInitiativeWillingnessToTakeResponsibilitiesFeedback(dto.getInitiativeWillingnessToTakeResponsibilitiesFeedback());
        evaluation.setAttendanceConsistencyInWorkFeedback(dto.getAttendanceConsistencyInWorkFeedback());
        evaluation.setTeamWorkCooperationFeedback(dto.getTeamWorkCooperationFeedback());
        evaluation.setOrganizingTimeManagementFeedback(dto.getOrganizingTimeManagementFeedback());
        evaluation.setAttitudeTowardsWorkFeedback(dto.getAttitudeTowardsWorkFeedback());
        evaluation.setWellVersedWithCompanyPoliciesFeedback(dto.getWellVersedWithCompanyPoliciesFeedback());
        evaluation.setThoroughWithCompanyCodeOfConductFeedback(dto.getThoroughWithCompanyCodeOfConductFeedback());
        evaluation.setRemarks(dto.getRemarks());

        return probationEvaluationSixRepository.save(evaluation);
    }

    //Fetching
    public List<ProbationEvaluation> getEvaluationsByEmpCode(String empCode) {
        Employee employee = employeeRepository.findByEmpCode(empCode)
                .orElseThrow(() -> new RuntimeException("Employee not found with empCode: " + empCode));

        return probationEvaluationRepository.findByEmployee_EmpCode(empCode);
    }

    @Transactional
    public ProbationRecordDTO createProbationRecord(ProbationRecordDTO dto) {
        ProbationRecord record = new ProbationRecord();

        Employee employee = employeeRepository.findByEmpCode(dto.getEmpCode())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        record.setEmployee(employee);

        // Set ProbationEvaluation - fetch by ID if given, else fetch by empCode and pick the first
        if (dto.getProbationEvaluationId() == null) {
            List<ProbationEvaluation> evals = probationEvaluationRepository.findByEmployee_EmpCode(dto.getEmpCode());
            if (!evals.isEmpty()) {
                record.setProbationEvaluation(evals.get(0));
            }
        } else {
            ProbationEvaluation evaluation = probationEvaluationRepository.findById(dto.getProbationEvaluationId())
                    .orElse(null);
            record.setProbationEvaluation(evaluation);
        }

        if (dto.getProbationEvaluationSixMonthsId() != null) {
            ProbationEvaluationSixMonths evaluationSix = probationEvaluationSixRepository.findById(dto.getProbationEvaluationSixMonthsId())
                    .orElse(null);
            record.setProbationEvaluationSixMonths(evaluationSix);
        }

        record.setActualProbationEndDate(dto.getActualProbationEndDate());
        record.setManagerEmpCode(dto.getManagerEmpCode());
        record.setHrEmpCode(dto.getHrEmpCode());

        String status = dto.getStatus();
        record.setR1ApprovalStatus(dto.getR1ApprovalStatus());
        record.setHrStatus(dto.getHrStatus());
        record.setStatus(status);

        record.setComments(dto.getComments());

        if ("Terminated".equalsIgnoreCase(status)) {
            record.setTerminationDate(dto.getTerminationDate());
        } else {
            record.setTerminationDate(null);
        }

        if ("Extended Probation".equalsIgnoreCase(status)) {
            int newExtensionCount = dto.getTotalNumberExtended() + 1;
            record.setTotalNumberExtended(newExtensionCount);

            LocalDate newEndDate = dto.getCurrentProbationEndDate().plusMonths(3);
            record.setCurrentProbationEndDate(newEndDate);

            if (dto.getExtendedDate() != null) {
                record.setExtendedDate(dto.getExtendedDate());
            } else {
                record.setExtendedDate(LocalDate.now());
            }
        } else {
            record.setTotalNumberExtended(dto.getTotalNumberExtended());
            record.setCurrentProbationEndDate(dto.getCurrentProbationEndDate());

            // Set extended date also if provided in DTO (even if not extended probation)
            record.setExtendedDate(dto.getExtendedDate());
        }

        // Use probationDays from DTO directly, or calculate here if preferred
        if (dto.getProbationDays() != null && dto.getProbationDays() > 0) {
            record.setProbationDays(dto.getProbationDays());
        } else {
            LocalDate startDate = employee.getDateOfJoining();
            LocalDate endDate = dto.getActualProbationEndDate();

            if (startDate != null && endDate != null) {
                long days = ChronoUnit.DAYS.between(startDate, endDate);
                record.setProbationDays((int) days);
            }
        }

        ProbationRecord saved = probationRecordRepository.save(record);

        ProbationRecordDTO savedDto = new ProbationRecordDTO();
        savedDto.setId(saved.getId());
        savedDto.setEmpCode(saved.getEmployee().getEmpCode());
        savedDto.setEmployeeName(saved.getEmployee().getFirstName());
        savedDto.setProbationEvaluationId(saved.getProbationEvaluation() != null ? saved.getProbationEvaluation().getId() : null);
        savedDto.setProbationEvaluationSixMonthsId(saved.getProbationEvaluationSixMonths() != null ? saved.getProbationEvaluationSixMonths().getId() : null);
        savedDto.setActualProbationEndDate(saved.getActualProbationEndDate());
        savedDto.setExtendedDate(saved.getExtendedDate());
        savedDto.setTerminationDate(saved.getTerminationDate());
        savedDto.setComments(saved.getComments());
        savedDto.setTotalNumberExtended(saved.getTotalNumberExtended());
        savedDto.setProbationDays(saved.getProbationDays());
        savedDto.setCurrentProbationEndDate(saved.getCurrentProbationEndDate());
        savedDto.setR1ApprovalStatus(saved.getR1ApprovalStatus());
        savedDto.setHrStatus(saved.getHrStatus());
        savedDto.setStatus(saved.getStatus());
        savedDto.setManagerEmpCode(saved.getManagerEmpCode());
        savedDto.setHrEmpCode(saved.getHrEmpCode());
        savedDto.setCreatedAt(saved.getCreatedAt());
        savedDto.setUpdatedAt(saved.getUpdatedAt());

        return savedDto;
    }

    //Fetching the latest probation record
    public ProbationRecordDTO getLatestRecordByEmpCode(String empCode) {
        ProbationRecord record = probationRecordRepository
                .findTopByEmployee_EmpCodeOrderByCreatedAtDesc(empCode)
                .orElseThrow(() -> new RuntimeException("No probation record found for empCode: " + empCode));

        return new ProbationRecordDTO(
                record.getId(),
                record.getEmployee().getEmpCode(),
                record.getEmployee().getFirstName(),
                record.getProbationEvaluation() != null ? record.getProbationEvaluation().getId() : null,
                record.getProbationEvaluationSixMonths() != null ? record.getProbationEvaluationSixMonths().getId() : null,
                record.getActualProbationEndDate(),
                record.getExtendedDate(),
                record.getTerminationDate(),
                record.getTotalNumberExtended(),
                record.getProbationDays(),
                record.getCurrentProbationEndDate(),
                record.getR1ApprovalStatus(),
                record.getHrStatus(),
                record.getStatus(),
                record.getManagerEmpCode(),
                record.getHrEmpCode(),
                record.getComments(),
                record.getCreatedAt(),
                record.getUpdatedAt()
        );
    }

    public Optional<ProbationEvaluation> getLatestEvaluationByEmpCode(String empCode) {
        Optional<Employee> employeeOptional = employeeRepository.findByEmpCode(empCode);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            return probationEvaluationRepository
                    .findByEmployeeOrderByCreatedAtDesc(employee)
                    .stream()
                    .findFirst(); // Return the latest record by createdAt
        }
        return Optional.empty();
    }

    public Optional<ProbationEvaluationSixMonths> getLatestEvaluationSixByEmpCode(String empCode) {
        Optional<Employee> employeeOptional = employeeRepository.findByEmpCode(empCode);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            return probationEvaluationSixRepository
                    .findByEmployeeOrderByCreatedAtDesc(employee)
                    .stream()
                    .findFirst(); // Return the latest record by createdAt
        }
        return Optional.empty();
    }


    public Optional<ProbationEvaluation> getEvaluationByIdAndEmpCode(Long id, String empCode) {
        return probationEvaluationRepository.findByIdAndEmployee_EmpCode(id, empCode);
    }

    public Optional<ProbationEvaluationSixMonths> getEvaluationByIdAndEmpCodeSix(Long id, String empCode) {
        return probationEvaluationSixRepository.findByIdAndEmployee_EmpCode(id, empCode);
    }

    @Transactional
    public List<ProbationRecord> getAllRecordsByEmpCode(String empCode) {
        return probationRecordRepository.findByEmployee_EmpCodeOrderByCreatedAtAsc(empCode);
    }
}
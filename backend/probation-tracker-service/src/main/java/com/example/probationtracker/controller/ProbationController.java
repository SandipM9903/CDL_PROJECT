package com.example.probationtracker.controller;

import com.example.probationtracker.dto.*;
import com.example.probationtracker.model.ProbationEvaluation;
import com.example.probationtracker.model.ProbationEvaluationSixMonths;
import com.example.probationtracker.model.ProbationRecord;
import com.example.probationtracker.service.ProbationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/probation")
@CrossOrigin(origins = "http://localhost:3000")
public class ProbationController {

    private final ProbationService probationService;

    @Autowired
    public ProbationController(ProbationService probationService) {
        this.probationService = probationService;
    }

    @GetMapping("/summary")
    public ResponseEntity<List<EmployeeProbationSummaryDTO>> getProbationSummary(
            @RequestParam(name = "filter", defaultValue = "All") String filter) {
        List<EmployeeProbationSummaryDTO> summary = probationService.getAllEmployeesProbationSummary(filter);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/employees/{empCode}/details")
    public ResponseEntity<EmployeeFullDetailsDTO> getEmployeeDetails(@PathVariable String empCode) {
        try {
            EmployeeFullDetailsDTO details = probationService.getEmployeeFullDetails(empCode);
            return ResponseEntity.ok(details);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

//    @PostMapping("/evaluate")
//    public ResponseEntity<String> submitProbationEvaluation(@RequestBody ProbationEvaluationRequestDTO requestDTO) {
//        try {
//            // Add logging to verify the request data
//            System.out.println("Received evaluation request for empCode: " + requestDTO.getEmpCode());
//            System.out.println("Full request: " + requestDTO.toString());
//
//            probationService.submitProbationEvaluation(requestDTO);
//            return ResponseEntity.status(HttpStatus.CREATED).body("Probation evaluations submitted successfully.");
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error submitting evaluation: " + e.getMessage());
//        }
//    }

    @PostMapping("/action")
    public ResponseEntity<String> handleProbationAction(@RequestBody ProbationActionRequestDTO requestDTO) {
        try {
            probationService.handleProbationAction(requestDTO);
            return ResponseEntity.ok("Probation action '" + requestDTO.getActionType() + "' processed successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing probation action: " + e.getMessage());
        }
    }

    @PostMapping("/initialize-records")
    public ResponseEntity<String> initializeProbationRecords() {
        probationService.initializeProbationRecords();
        return ResponseEntity.ok("Probation records initialized for existing employees.");
    }

    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(
            @RequestParam(name = "searchTerm", required = false) String searchTerm,
            @RequestParam(name = "role", required = false) String roleFilter,
            @RequestParam(name = "status", defaultValue = "All") String statusFilter) {
        List<EmployeeDTO> employees = probationService.getAllEmployees(searchTerm, roleFilter, statusFilter);
        return ResponseEntity.ok(employees);
    }

    @PostMapping("/test-evaluation")
    public ResponseEntity<String> testEvaluation(@RequestBody ProbationEvaluationRequestDTO requestDTO) {
        return ResponseEntity.ok("Received empCode: " + requestDTO.getEmpCode());
    }

    //Needed
    @PostMapping("/evaluate")
    public ResponseEntity<ProbationEvaluation> createProbationEvaluation(@RequestBody ProbationEvaluationDTO dto) {
        ProbationEvaluation createdEvaluation = probationService.createProbationEvaluation(dto);
        return ResponseEntity.ok(createdEvaluation);
    }

    @PostMapping("/evaluate/six")
    public ResponseEntity<ProbationEvaluationSixMonths> createProbationEvaluationSix(@RequestBody ProbationEvaluationSixDto dto) {
        ProbationEvaluationSixMonths createdEvaluation = probationService.createProbationEvaluationSix(dto);
        return ResponseEntity.ok(createdEvaluation);
    }

    @GetMapping("/{empCode}")
    public ResponseEntity<List<ProbationEvaluation>> getEvaluationsByEmpCode(@PathVariable String empCode) {
        List<ProbationEvaluation> evaluations = probationService.getEvaluationsByEmpCode(empCode);

        if (evaluations.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(evaluations);
        }
    }

    @GetMapping("/exists/{empCode}")
    public ResponseEntity<Boolean> probationEvaluationExists(@PathVariable String empCode) {
        List<ProbationEvaluation> evaluations = probationService.getEvaluationsByEmpCode(empCode);
        boolean exists = !evaluations.isEmpty();
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/probation-record")
    public ResponseEntity<ProbationRecordDTO> createProbationRecord(@RequestBody ProbationRecordDTO probationRecordDTO) {
        ProbationRecordDTO savedRecord = probationService.createProbationRecord(probationRecordDTO);
        return new ResponseEntity<>(savedRecord, HttpStatus.CREATED);
    }

    @GetMapping("/probation-record/{empCode}")
    public ResponseEntity<ProbationRecordDTO> getLatestRecordByEmpCode(@PathVariable String empCode) {
        try {
            ProbationRecordDTO recordDTO = probationService.getLatestRecordByEmpCode(empCode);
            return ResponseEntity.ok(recordDTO);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/probation-evaluation-three/{empCode}/{id}")
    public ResponseEntity<ProbationEvaluation> getEvaluationByEmpCodeAndId(
            @PathVariable String empCode,
            @PathVariable Long id) {
        return probationService.getEvaluationByIdAndEmpCode(id, empCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/probation-evaluation-six/{empCode}/{id}")
    public ResponseEntity<ProbationEvaluationSixMonths> getEvaluationByEmpCodeAndIdSix(
            @PathVariable String empCode,
            @PathVariable Long id) {
        return probationService.getEvaluationByIdAndEmpCodeSix(id, empCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("probation-evaluation/latest/{empCode}")
    public ResponseEntity<ProbationEvaluation> getLatestEvaluationByEmpCode(@PathVariable String empCode) {
        return probationService.getLatestEvaluationByEmpCode(empCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("probation-evaluation-six/latest/{empCode}")
    public ResponseEntity<ProbationEvaluationSixMonths> getLatestEvaluationSixByEmpCode(@PathVariable String empCode) {
        return probationService.getLatestEvaluationSixByEmpCode(empCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/probation-record/history/{empCode}")
    public List<ProbationRecord> getAllProbationRecords(@PathVariable String empCode) {
        return probationService.getAllRecordsByEmpCode(empCode);
    }
}
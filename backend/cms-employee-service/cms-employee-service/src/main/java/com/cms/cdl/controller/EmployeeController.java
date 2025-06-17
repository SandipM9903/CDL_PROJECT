package com.cms.cdl.controller;

import com.cms.cdl.beans.DepartmentBean;
import com.cms.cdl.beans.FileAndContentTypeBean;
import com.cms.cdl.dto.request_dto.EmpReqDTO;
import com.cms.cdl.dto.response_dto.EmployeeResponseITDecDTO;
import com.cms.cdl.service.service_interface.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/employee")
@CrossOrigin("*")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    /*
    -- Add Employee
    */

    // http://localhost:8086/employee
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.ALL_VALUE})
    public ResponseEntity<?> addEmployee(@RequestParam("employeeDTO") String employeeReqDTO, @RequestPart(value = "image", required = false)MultipartFile profileImage) throws IOException {
        EmpReqDTO empReqDTO = new ObjectMapper().readValue(employeeReqDTO, EmpReqDTO.class);
        return new ResponseEntity<>(employeeService.addEmployees(empReqDTO, profileImage), HttpStatus.OK);
    }
    /*
    -- update employee
    */

    // http://localhost:8086/employee/
    @PutMapping(value = "/{empId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.ALL_VALUE})
    public ResponseEntity<?> updateEmployee(@PathVariable("empId") long empId , @RequestParam("employeeDTO") String employeeReqDTO, @RequestPart(value = "image", required = false)MultipartFile profileImage) throws IOException {
        EmpReqDTO empReqDTO = new ObjectMapper().readValue(employeeReqDTO, EmpReqDTO.class);
        return new ResponseEntity<>(employeeService.updateEmployee(empId, empReqDTO, profileImage), HttpStatus.OK);
    }

    /*
    -- update employee profile image
    */

    // http://localhost:8086/employee/update/img/
    @PutMapping(value = "/update/img/{empId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.ALL_VALUE})
    public ResponseEntity<?> updateEmployeeProfileImg(@PathVariable("empId") long empId, @RequestPart("image") MultipartFile profileImage) throws IOException {
        return new ResponseEntity<>(employeeService.updateProfileImage(empId, profileImage), HttpStatus.OK);
    }

    /*
    -- delete employee
    */

    // http://localhost:8086/employee/
    @DeleteMapping("/{empId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("empId") long empId){
        return new ResponseEntity<>(employeeService.deleteEmployee(empId), HttpStatus.OK);
    }

    /*
    -- fetch the employee details by employee id
    */

    // http://localhost:8086/employee/
    @GetMapping("/{empId}")
    public ResponseEntity<?> getEmployeeInfoByEmpId(@PathVariable long empId) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(employeeService.getEmployeeByEmployeeId(empId), HttpStatus.OK);
    }

    /*
    -- fetch the employee details by employee code
    */

    // http://localhost:8086/employee/eCode/
    @GetMapping("/eCode/{empCode}")
    public ResponseEntity<?> getEmployeeInfoByEmpCode(@PathVariable String empCode) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(employeeService.getEmployeeByEmployeeCode(empCode), HttpStatus.OK);
    }

    /*
    -- fetch the employee details by email id
    */

    // http://localhost:8086/employee/by/email/
    @GetMapping("/by/email/{emailId}")
    public ResponseEntity<?> getEmployeeByEmailId(@PathVariable String emailId) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(employeeService.getEmployeeByEmailId(emailId), HttpStatus.OK);
    }

    /*
    -- fetch all employees with pagination
    */

    // http://localhost:8086/employee/getAll/
    @GetMapping("/getAll/{page}")
    public ResponseEntity<?> getAllEmployeesWithPagination(@PathVariable("page") int page) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(employeeService.getAllEmployeesWithPagination(page), HttpStatus.OK);
    }

    /*
    -- fetch all employees
    */

    // http://192.168.249.13:8086/employee/getAll
    // http://localhost:8086/employee/getAll
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllEmployees() throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
    }

    /*
    -- fetch employee birthday data
    */

    // http://localhost:8086/employee/birthday/wishes/data
    @GetMapping("/birthday/wishes/data")
    public ResponseEntity<?> birthdayWishesData() throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(employeeService.getBirthdayWishesData(), HttpStatus.OK);
    }

    /*
    -- fetch work anniversary of employees
    */

    // http://localhost:8086/employee/work/anniversary
    @GetMapping("/work/anniversary")
    public ResponseEntity<?> celebratingWorkAnniversary() throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(employeeService.getWorkAnniversaryData(), HttpStatus.OK);
    }

    /*
    -- fetch employee documents
    */

    // http://localhost:8086/employee/get/myDocs/
    @GetMapping("/get/myDocs/{empId}")
    public ResponseEntity<?> getMyDocuments(@PathVariable("empId") long empId){
        return new ResponseEntity<>(employeeService.getMyDocuments(empId), HttpStatus.OK);
    }

    /*
    -- read employee documents
    */

    // http://localhost:8086/employee/open/docs/{docId}
    @GetMapping("/open/docs/{docId}")
    public ResponseEntity<?> openMyDocuments(@PathVariable("docId") Long docId) throws ExecutionException, InterruptedException {
        FileAndContentTypeBean file = employeeService.openAndDownloadMyDocuments(docId);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf(file.getContentType()))
                .body(file.getFile());
    }

    /*
    -- fetch employees based on employee location
    */

    // http://localhost:8086/employee/loc/based//
    @GetMapping("/loc/based/{locationId}/{page}")
    public ResponseEntity<?> getEmployeesLocationBased(@PathVariable("locationId") Long locationId, @PathVariable("page") int page) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(employeeService.getEmployeesLocationBased(locationId, page), HttpStatus.OK);
    }

    /*
    -- fetch HR hired employees based on HR employee id
    */

    // http://localhost:8086/employee/hiring/hr/
    @GetMapping("/hiring/hr/{empCode}")
    public ResponseEntity<?> getHRHiredEmployees(@PathVariable("empCode") String empCode) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(employeeService.getHRHiredEmployees(empCode), HttpStatus.OK);
    }

    /**
     * fetch all main department and respective sub_department
     * for help_ticketing
     *
     * @return
     */
    // http://localhost:8086/employee/all-departments
    @GetMapping("/all-departments")
    public ResponseEntity<?> getAllDepartmentsData() {
        List<DepartmentBean> departmentsList = employeeService.getAllDepartments();
        return new ResponseEntity<>( departmentsList, HttpStatus.OK);
    }

    /**
     * fetch all employees based on reporting manager emp_code
     *
     * @param empCode
     * @return
     */
    // http://localhost:8086/employee/report/to/
    @GetMapping("/report/to/{empCode}")
    public ResponseEntity<?> fetchEmployeesBasedOnReportingManagerEmpCode(@PathVariable("empCode") String empCode) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(employeeService.fetchEmployeesBasedOnRepMgrEmpCode(empCode), HttpStatus.OK);
    }

    // http://localhost:8086/employee/allEmp/itAdmin
    @GetMapping("/allEmp/itAdmin")
    public ResponseEntity<?> fetchAllEmployeeForITAdmin() {
        List<EmployeeResponseITDecDTO> employeeResponseDTOList = employeeService.getAllEmployeeForAdmin();
        return new ResponseEntity<>( employeeResponseDTOList, HttpStatus.OK);
    }

    // password creation API for Document to save in DMS
    // http://localhost:8086/employee/passwd/
    @GetMapping("/passwd/{empCode}")
    public ResponseEntity<?> fetchPassword(@PathVariable("empCode") String empCode){
        return new ResponseEntity<>(employeeService.fetchPasswordForDocument(empCode), HttpStatus.OK);
    }

    /**
     * fetch employees under Reporting Manager
     *
     * @param empCode
     * @return
     */
    // http://localhost:8086/employee/team/hierarchy/
    @GetMapping("/team/hierarchy/{empCode}")
    public ResponseEntity<?> fetchEmployeesUnderManager(@PathVariable("empCode") String empCode) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(employeeService.fetchEmployeesUnderManager(empCode), HttpStatus.OK);
    }


    /**
     * fetch all team members under R1 or R2
     *
     * @param empCode
     * @return
     */
    // http://localhost:8086/employee/team/
    @GetMapping("/team/{empCode}")
    public ResponseEntity<?> fetchAllTeamMembers(@PathVariable("empCode") String empCode) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(employeeService.fetchAllTeamMembers(empCode), HttpStatus.OK);
    }

}

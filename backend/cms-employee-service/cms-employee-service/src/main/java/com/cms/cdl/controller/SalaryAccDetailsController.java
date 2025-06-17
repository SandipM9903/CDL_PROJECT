package com.cms.cdl.controller;

import com.cms.cdl.dto.request_dto.SalaryAccDetailsReqDTO;
import com.cms.cdl.service.service_interface.SalaryAccDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.AttributeNotFoundException;

@RestController
@RequestMapping("/employee")
@CrossOrigin("*")
public class SalaryAccDetailsController {
    @Autowired
    SalaryAccDetailsService salaryAccDetailsService;

    // http://localhost:8086/employee/sal/update/
    @PutMapping("/sal/update/{salaryAccDetailsId}")
    public ResponseEntity<?> updateSalaryDetails(@PathVariable("salaryAccDetailsId") Long salaryAccDetailsId, @RequestBody SalaryAccDetailsReqDTO salaryAccDetailsReqDTO) throws AttributeNotFoundException {
        return new ResponseEntity<>(salaryAccDetailsService.updateSalaryDetails(salaryAccDetailsId, salaryAccDetailsReqDTO), HttpStatus.OK);
    }

}

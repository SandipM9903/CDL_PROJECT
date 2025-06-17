package com.cms.cdl.controller;

import com.cms.cdl.dto.request_dto.DependentDetailsReqDTO;
import com.cms.cdl.model.DependentDetails;
import com.cms.cdl.service.service_interface.DependentDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.AttributeNotFoundException;

@RestController
@RequestMapping("/employee")
@CrossOrigin("*")
public class DependentDetailsController {
    @Autowired
    DependentDetailsService dependentDetailsService;

    // http://localhost:8086/employee/dependent/
    @PutMapping("/dependent/update/{id}")
    public ResponseEntity<?> updateDependentDetails(@PathVariable("id") Long id, @RequestBody DependentDetailsReqDTO dependentDetailsReqDTO) throws AttributeNotFoundException {
        return new ResponseEntity<>(dependentDetailsService.updateDependentDetails(id, dependentDetailsReqDTO), HttpStatus.OK);
    }
}

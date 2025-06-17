package com.cms.cdl.controller;

import com.cms.cdl.dto.response_dto.DesignationResDTO;
import com.cms.cdl.service.service_interface.DesignationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/designation")
@CrossOrigin("*")
public class DesignationController {
    @Autowired
    DesignationService designationService;

    // http://localhost:8086/designation/getAll
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllDesignations() {
        List<DesignationResDTO> designationResDTOList = designationService.fetchAllDesignation();
        return new ResponseEntity<>( designationResDTOList, HttpStatus.OK);
    }

}

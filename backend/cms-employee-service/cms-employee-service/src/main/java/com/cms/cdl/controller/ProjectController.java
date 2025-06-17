package com.cms.cdl.controller;

import com.cms.cdl.dto.response_dto.ProjectResDTO;
import com.cms.cdl.service.service_interface.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/project")
@CrossOrigin("*")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    // http://localhost:8086/project/getAll
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllProjects() {
        List<ProjectResDTO> projectResDTOList = projectService.fetchAllProject();
        return new ResponseEntity<>( projectResDTOList, HttpStatus.OK);
    }
}

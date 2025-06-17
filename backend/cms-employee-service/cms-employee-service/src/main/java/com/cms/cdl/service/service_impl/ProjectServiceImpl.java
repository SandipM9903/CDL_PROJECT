package com.cms.cdl.service.service_impl;

import com.cms.cdl.dto.response_dto.ProjectResDTO;
import com.cms.cdl.repo.ProjectRepo;
import com.cms.cdl.service.service_interface.ProjectService;
import com.cms.cdl.utils.ModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    ProjectRepo projectRepo;
    @Override
    public List<ProjectResDTO> fetchAllProject() {
        return ModelConverter.convertToProjectResDTOList(projectRepo.findAll());
    }

}

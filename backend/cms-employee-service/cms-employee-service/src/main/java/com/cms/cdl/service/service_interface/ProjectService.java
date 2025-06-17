package com.cms.cdl.service.service_interface;

import com.cms.cdl.dto.response_dto.ProjectResDTO;

import java.util.List;

public interface ProjectService {
    List<ProjectResDTO> fetchAllProject();

}

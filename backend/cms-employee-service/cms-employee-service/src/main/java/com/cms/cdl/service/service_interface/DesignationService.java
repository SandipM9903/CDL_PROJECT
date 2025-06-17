package com.cms.cdl.service.service_interface;

import com.cms.cdl.dto.response_dto.DesignationResDTO;

import java.util.List;

public interface DesignationService {
    List<DesignationResDTO> fetchAllDesignation();
}

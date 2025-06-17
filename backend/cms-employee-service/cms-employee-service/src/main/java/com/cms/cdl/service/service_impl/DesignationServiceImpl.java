package com.cms.cdl.service.service_impl;

import com.cms.cdl.dto.response_dto.DesignationResDTO;
import com.cms.cdl.repo.DesignationRepo;
import com.cms.cdl.service.service_interface.DesignationService;
import com.cms.cdl.utils.ModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DesignationServiceImpl implements DesignationService {
    @Autowired
    DesignationRepo designationRepo;
    @Override
    public List<DesignationResDTO> fetchAllDesignation() {
        return ModelConverter.convertToDegResDTOList(designationRepo.findAll());
    }
}

package com.cms.cdl.service.service_impl;

import com.cms.cdl.dto.request_dto.DependentDetailsReqDTO;
import com.cms.cdl.dto.response_dto.DependentDetailsResDTO;
import com.cms.cdl.model.DependentDetails;
import com.cms.cdl.repo.DependentDetailsRepo;
import com.cms.cdl.service.service_interface.DependentDetailsService;
import com.cms.cdl.utils.ModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.AttributeNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class DependentDetailsServiceImpl implements DependentDetailsService {
    @Autowired
    DependentDetailsRepo dependentDetailsRepo;


    @Override
    public List<DependentDetailsResDTO> getDependentDetails(long empId) throws AttributeNotFoundException {
        Optional<List<DependentDetails>> dependentDetails = dependentDetailsRepo.findByEmployee_Id(empId);

        if(dependentDetails.isEmpty()){
            throw new AttributeNotFoundException("Salary account details not found for employee ID : " + empId);
        }
        return ModelConverter.convertToDependentDetailsResDTOList(dependentDetails.get());
    }

    @Override
    public DependentDetailsResDTO updateDependentDetails(long id, DependentDetailsReqDTO dependentDetailsReqDTO) throws AttributeNotFoundException {
        Optional<DependentDetails> existingDetailsOpt = dependentDetailsRepo.findById(id);
        if (existingDetailsOpt.isPresent()) {
            DependentDetails existingDependentDetails = existingDetailsOpt.get();
            DependentDetails dependentDetails = ModelConverter.convertToDependentDetails(dependentDetailsReqDTO);
            Optional.ofNullable(dependentDetails.getDependentName()).ifPresent(existingDependentDetails::setDependentName);
            Optional.ofNullable(dependentDetails.getDependentRelationship()).ifPresent(existingDependentDetails::setDependentRelationship);
            Optional.ofNullable(dependentDetails.getDependentDateOfBirth()).ifPresent(existingDependentDetails::setDependentDateOfBirth);
            return ModelConverter.convertToDependentDetailsResDTO(dependentDetailsRepo.save(existingDependentDetails));
        }
        return null;
    }

    @Override
    public boolean deleteDependentDetails(long id) throws AttributeNotFoundException {
        Optional<DependentDetails> dependentDetailsOpt = dependentDetailsRepo.findById(id);

        if (dependentDetailsOpt.isEmpty()) {
            throw new AttributeNotFoundException("Salary account details not found for ID: " + id);
        }

        dependentDetailsRepo.deleteById(id);

        return true;
    }
}

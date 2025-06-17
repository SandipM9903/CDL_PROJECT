package com.cms.cdl.service.service_interface;

import com.cms.cdl.dto.request_dto.DependentDetailsReqDTO;
import com.cms.cdl.dto.response_dto.DependentDetailsResDTO;

import javax.management.AttributeNotFoundException;
import java.util.List;

public interface DependentDetailsService {
    List<DependentDetailsResDTO> getDependentDetails(long empId) throws AttributeNotFoundException;
    DependentDetailsResDTO updateDependentDetails(long id, DependentDetailsReqDTO dependentDetailsReqDTO) throws AttributeNotFoundException;
    boolean deleteDependentDetails(long id) throws AttributeNotFoundException;

}

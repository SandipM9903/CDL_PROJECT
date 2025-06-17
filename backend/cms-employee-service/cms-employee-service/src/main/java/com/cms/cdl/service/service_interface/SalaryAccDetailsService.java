package com.cms.cdl.service.service_interface;

import com.cms.cdl.dto.request_dto.SalaryAccDetailsReqDTO;
import com.cms.cdl.dto.response_dto.SalaryAccDetailsResDTO;

import javax.management.AttributeNotFoundException;

public interface SalaryAccDetailsService {
    SalaryAccDetailsResDTO getEmployeeSalaryDetails(long empId) throws AttributeNotFoundException;

    SalaryAccDetailsResDTO saveEmployeeSalaryDetails(SalaryAccDetailsReqDTO salaryAccDetailsReqDTO);

    SalaryAccDetailsResDTO updateSalaryDetails(long salaryAccDetailsId, SalaryAccDetailsReqDTO salaryAccDetailsReqDTO) throws AttributeNotFoundException;

    boolean deleteEmployeeSalaryDetails(long empId) throws AttributeNotFoundException;
}

package com.cms.cdl.dto.response_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpHierarchyResDTO {
    private long empId;
    private String empCode;
    private String firstName;
    private String middleName;
    private String lastName;
    private String fullNameAsAadhaar;
    private String emailId;
    private DesignationResDTO designationResDTO;
}

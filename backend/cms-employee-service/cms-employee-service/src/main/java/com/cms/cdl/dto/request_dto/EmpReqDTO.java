package com.cms.cdl.dto.request_dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmpReqDTO extends BaseEntityReqDTO{
    private String empCode;
    private long userId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String fullNameAsAadhaar;
    private String age;
    private String emailId;
    private String password;
    private String roles;
    private String reportingManager;
    private String reportTo;
    private String reportingManagerEmailId;
    private String dateOfJoining;
    private String dateOfBirth;
    private String primaryContactNo;
    private String secondaryContactNo;
    private String emergencyContactNo;
    private String emergencyContactName;
    private String relationWithEmergencyContact;
    private String personalEmailId;
    private String passportNumber;
    private String aboutEmp;
    private String maritalStatus;
    private String gender;
    private String bloodGroup;
    private boolean status;
    private String probationPeriod;
    private String dateOfConfirmation;
    private boolean resignation;
    private String dateOfLeaving;
    private String noticePeriod;
    private String nomineeName;
    private boolean ptApplicability;
    private boolean mlwfApplicability;
    private String gmcApplicability;
    private String gtlApplicability;
    private String gpaApplicability;
    private String wcApplicability;
    private String hiringHr;
    private long categoryId;
    private long classificationId;
    private long eeSubgroupId;
    private long employmentStatusId;
    private long generationTypeId;
    private long gradeId;
    private long payrollAreaTypeId;
    private long buHeadId;
    private long mainDeptId;
    private long subDeptId;
    private long projectId;
    private long orgCode;
    private long designationId;
    private long orgHierarchyId;

    private List<DependentDetailsReqDTO> dependentDetailsReqDTOS;
    private SalaryAccDetailsReqDTO salaryAccDetailsReqDTO;
}

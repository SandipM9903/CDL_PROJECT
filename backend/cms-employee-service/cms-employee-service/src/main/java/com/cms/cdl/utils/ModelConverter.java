package com.cms.cdl.utils;

import com.cms.cdl.dto.request_dto.*;
import com.cms.cdl.dto.response_dto.*;
import com.cms.cdl.model.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ModelConverter {
    private static final ModelMapper modelMapper = new ModelMapper();

    // converting employee request DTO to model class

    public static Employee convertToEmployee(EmpReqDTO empReqDTO){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate doj = null;
        LocalDate dob = null;
        LocalDate dateOfConfirmation = null;
        LocalDate dateOfLeaving = null;


        // check date of joining is present or not
        if(empReqDTO.getDateOfJoining() != null){
            doj = LocalDate.parse(empReqDTO.getDateOfJoining(), formatter);
        }

        // check date of birth is present or not
        if(empReqDTO.getDateOfBirth() != null){
            dob = LocalDate.parse(empReqDTO.getDateOfBirth(), formatter);
        }

        // check date_of_confirmation is present or not
        if(empReqDTO.getDateOfConfirmation()!=null){
            dateOfConfirmation = LocalDate.parse(empReqDTO.getDateOfConfirmation(), formatter);
        }

        // check date_of_leaving is present or not
        if(empReqDTO.getDateOfLeaving()!=null){
            dateOfLeaving = LocalDate.parse(empReqDTO.getDateOfLeaving(), formatter);
        }

        Employee employee = new Employee();
        employee.setEmpCode(empReqDTO.getEmpCode());
        employee.setUserId(empReqDTO.getUserId());
        employee.setFirstName(empReqDTO.getFirstName());
        employee.setLastName(empReqDTO.getLastName());
        employee.setFullNameAsAadhaar(empReqDTO.getFullNameAsAadhaar());
        employee.setAge(empReqDTO.getAge());
        employee.setEmailId(empReqDTO.getEmailId());
        employee.setPassword(empReqDTO.getPassword());
        employee.setRoles(empReqDTO.getRoles());
        employee.setReportingManager(empReqDTO.getReportingManager());
        employee.setReportTo(empReqDTO.getReportTo());
        employee.setReportingManagerEmailId(empReqDTO.getReportingManagerEmailId());
        employee.setDateOfJoining(doj);
        employee.setDateOfBirth(dob);
        employee.setPrimaryContactNo(empReqDTO.getPrimaryContactNo());
        employee.setSecondaryContactNo(empReqDTO.getSecondaryContactNo());
        employee.setEmergencyContactNo(empReqDTO.getEmergencyContactNo());
        employee.setEmergencyContactName(empReqDTO.getEmergencyContactName());
        employee.setRelationWithEmergencyContact(empReqDTO.getRelationWithEmergencyContact());
        employee.setPersonalEmailId(empReqDTO.getPersonalEmailId());
        employee.setPassportNumber(empReqDTO.getPassportNumber());
        employee.setAboutEmp(empReqDTO.getAboutEmp());
        employee.setMaritalStatus(empReqDTO.getMaritalStatus());
        employee.setGender(empReqDTO.getGender());
        employee.setBloodGroup(empReqDTO.getBloodGroup());
        employee.setStatus(empReqDTO.isStatus());
        employee.setProbationPeriod(empReqDTO.getProbationPeriod());
        employee.setDateOfConfirmation(dateOfConfirmation);
        employee.setResignation(empReqDTO.isResignation());
        employee.setDateOfLeaving(dateOfLeaving);
        employee.setNoticePeriod(empReqDTO.getNoticePeriod());
        employee.setNomineeName(empReqDTO.getNomineeName());
        employee.setPtApplicability(empReqDTO.isPtApplicability());
        employee.setMlwfApplicability(empReqDTO.isMlwfApplicability());
        employee.setGmcApplicability(empReqDTO.getGmcApplicability());
        employee.setGtlApplicability(empReqDTO.getGtlApplicability());
        employee.setGpaApplicability(empReqDTO.getGpaApplicability());
        employee.setWcApplicability(empReqDTO.getWcApplicability());
        employee.setHiringHr(empReqDTO.getHiringHr());

        // convert to dependent details
        if(empReqDTO.getDependentDetailsReqDTOS() != null){
            List<DependentDetails> dependentDetails = convertToDependentDetailsList(empReqDTO.getDependentDetailsReqDTOS(), employee);
            employee.setDependentDetails(dependentDetails);
        }

        // convert to salary account details
        if(empReqDTO.getSalaryAccDetailsReqDTO() != null){
            SalaryAccountDetails salaryAccountDetails = convertToSalaryAccDetails(empReqDTO.getSalaryAccDetailsReqDTO());
            employee.setSalaryAccountDetails(salaryAccountDetails);
        }


        return employee;
    }

    public static MainDepartment convertToMainDepartment(MainDeptReqDTO mainDeptReqDTO){
        return modelMapper.map(mainDeptReqDTO, MainDepartment.class);
    }

    public static SubDepartment convertToSubDepartment(SubDeptReqDTO subDeptReqDTO){
        return modelMapper.map(subDeptReqDTO, SubDepartment.class);
    }

    public static DependentDetails convertToDependentDetails(DependentDetailsReqDTO dependentDetailsReqDTO){
        DependentDetails dependentDetails = new DependentDetails();
        dependentDetails.setDependentName(dependentDetailsReqDTO.getDependentName());
        dependentDetails.setDependentRelationship(dependentDetailsReqDTO.getDependentRelationship());
        // Parse date if available
        if (dependentDetailsReqDTO.getDependentDateOfBirth() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dob = LocalDate.parse(dependentDetailsReqDTO.getDependentDateOfBirth(), formatter);
            dependentDetails.setDependentDateOfBirth(dob);
        }
        return dependentDetails;
    }

    public static List<DependentDetails> convertToDependentDetailsList(List<DependentDetailsReqDTO> dependentDetailsReqDTOS, Employee employee){
        return dependentDetailsReqDTOS.stream()
                .map(dto -> {
                    DependentDetails dependentDetails = new DependentDetails();
                    dependentDetails.setDependentName(dto.getDependentName());
                    dependentDetails.setDependentRelationship(dto.getDependentRelationship());
                    dependentDetails.setDependentDateOfBirth(LocalDate.parse(dto.getDependentDateOfBirth(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    dependentDetails.setEmployee(employee);
                    return dependentDetails;
                })
                .collect(Collectors.toList());
    }

    public static SalaryAccountDetails convertToSalaryAccDetails(SalaryAccDetailsReqDTO salaryAccDetailsReqDTO){
        return modelMapper.map(salaryAccDetailsReqDTO, SalaryAccountDetails.class);
    }

    public static Designation convertToDesignation(DesignationReqDTO designationReqDTO){
        return modelMapper.map(designationReqDTO, Designation.class);
    }

    public static Organization convertToOrganization(OrganizationReqDTO organizationReqDTO){
        return modelMapper.map(organizationReqDTO, Organization.class);
    }

    public static Project convertToProject(ProjectReqDTO projectReqDTO){
        return modelMapper.map(projectReqDTO, Project.class);
    }


/* -------------------------------------------------------------------------------------------------------------------------------------------------------- */

    // convert model classes to Response DTO

    public static EmpResDTO convertToEmpResDTO(Employee employee){
        EmpResDTO empResDTO = modelMapper.map(employee, EmpResDTO.class);
        empResDTO.setEmpId(employee.getId());
        return empResDTO;
    }

    public static List<EmployeeResponseITDecDTO> convertToEmpResDTOList(List<Employee> employees){
        return employees.stream().map(employee -> {
            EmployeeResponseITDecDTO employeeResponseITDecDTO = new EmployeeResponseITDecDTO();
            employeeResponseITDecDTO.setEmpId(employee.getId());
            employeeResponseITDecDTO.setFirstName(employee.getFirstName());
            return employeeResponseITDecDTO;
        }).collect(Collectors.toList());

    }

    public static DependentDetailsResDTO convertToDependentDetailsResDTO(DependentDetails dependentDetails){
        return modelMapper.map(dependentDetails, DependentDetailsResDTO.class);
    }

    public static List<DependentDetailsResDTO> convertToDependentDetailsResDTOList(List<DependentDetails> dependentDetails){
        return dependentDetails.stream()
                .map(dependentDetail -> modelMapper.map(dependentDetail, DependentDetailsResDTO.class))
                .collect(Collectors.toList());
    }

    public static SalaryAccDetailsResDTO convertToSalAccResDTO(SalaryAccountDetails salaryAccountDetails){
        SalaryAccDetailsResDTO salaryAccDetailsResDTO = modelMapper.map(salaryAccountDetails, SalaryAccDetailsResDTO.class);
        salaryAccDetailsResDTO.setSalaryAccDetailsId(salaryAccountDetails.getId());
        return salaryAccDetailsResDTO;
    }

    public static MainDeptResDTO convertToMainDeptResDTO(MainDepartment mainDepartment){
        MainDeptResDTO mainDeptResDTO = modelMapper.map(mainDepartment, MainDeptResDTO.class);
        mainDeptResDTO.setMainDeptId(mainDepartment.getId());
        return mainDeptResDTO;
    }

    public static SubDeptResDTO convertToSubDeptResDTO(SubDepartment subDepartment){
        SubDeptResDTO subDeptResDTO = modelMapper.map(subDepartment, SubDeptResDTO.class);
        subDeptResDTO.setSubDeptId(subDepartment.getId());
        return subDeptResDTO;
    }

    public static DesignationResDTO convertToDegResDTO(Designation designation){
        DesignationResDTO designationResDTO = modelMapper.map(designation, DesignationResDTO.class);
        designationResDTO.setDesignationId(designation.getId());
        return designationResDTO;
    }

    public static List<DesignationResDTO> convertToDegResDTOList(List<Designation> designation){
        return designation.stream()
                .map(deg -> modelMapper.map(deg, DesignationResDTO.class))
                .collect(Collectors.toList());
    }

    public static OrganizationResDTO convertToOrgResDTO(Organization organization){
        OrganizationResDTO organizationResDTO = modelMapper.map(organization, OrganizationResDTO.class);
        organizationResDTO.setOrgCode(organization.getId());
        return organizationResDTO;
    }

    public static ProjectResDTO convertToProjectResDTO(Project project){
        ProjectResDTO projectResDTO = modelMapper.map(project, ProjectResDTO.class);
        projectResDTO.setProjectId(project.getId());
        return projectResDTO;
    }

    public static List<ProjectResDTO> convertToProjectResDTOList(List<Project> projects){
        return projects.stream()
                .map(project -> modelMapper.map(project, ProjectResDTO.class))
                .collect(Collectors.toList());
    }

    public static OrgHierarchyResDTO convertToOrgHierarchyResDTO(OrgHierarchy orgHierarchy){
        OrgHierarchyResDTO orgHierarchyResDTO = modelMapper.map(orgHierarchy, OrgHierarchyResDTO.class);
        orgHierarchyResDTO.setOrgHierarchyId(orgHierarchy.getId());
        return orgHierarchyResDTO;
    }

    public static  EmploymentStatusResDTO convertToEmploymentStatusResDTO(EmploymentStatus employmentStatus){
        return modelMapper.map(employmentStatus, EmploymentStatusResDTO.class);
    }

    public static EmpHierarchyResDTO convertToReportingManagerResponseDTO(Employee employee){
        EmpHierarchyResDTO reportingMgrEmpResDTO = modelMapper.map(employee, EmpHierarchyResDTO.class);
        reportingMgrEmpResDTO.setEmpId(employee.getId());
        return reportingMgrEmpResDTO;
    }

    public static CategoryResDTO convertToCategoryResDTO(Category category){
        CategoryResDTO categoryResDTO = modelMapper.map(category, CategoryResDTO.class);
        categoryResDTO.setId(category.getId());
        return categoryResDTO;
    }



 /* ------------------------------------------------------------------------------------------------------------------------------------------------------- */

}

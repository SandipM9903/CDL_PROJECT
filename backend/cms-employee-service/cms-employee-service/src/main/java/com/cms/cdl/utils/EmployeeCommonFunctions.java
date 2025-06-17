package com.cms.cdl.utils;

import com.cms.cdl.beans.EmpAndUserResponse;
import com.cms.cdl.beans.FileAndContentTypeBean;
import com.cms.cdl.beans.FileAndObjectTypeBean;
import com.cms.cdl.dto.response_dto.*;
import com.cms.cdl.dto.user_dto.UserDTO;
import com.cms.cdl.model.*;
import com.cms.cdl.repo.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

@Component
public class EmployeeCommonFunctions {
    @Autowired
    EmployeeRepo employeeRepo;
    @Autowired
    MainDepartmentRepo mainDepartmentRepo;
    @Autowired
    SubDepartmentRepo subDepartmentRepo;
    @Autowired
    DependentDetailsRepo dependentDetailsRepo;
    @Autowired
    SalaryAccDetailsRepo salaryAccDetailsRepo;
    @Autowired
    DesignationRepo designationRepo;
    @Autowired
    OrganizationRepo organizationRepo;
    @Autowired
    ProjectRepo projectRepo;
    @Autowired
    OrgHierarchyRepo orgHierarchyRepo;
    @Autowired
    EmploymentStatusRepo employmentStatusRepo;
    @Autowired
    CategoryRepo categoryRepo;
    @Autowired
    UserOperations userOperations;
    @Autowired
    DocumentOperations documentOperations;
    @Autowired
    ObjectMapper objectMapper;

    public EmpResDTO returningEmployeeResponse(Employee employee) {
        EmpResDTO empResDTO;

        // fetching data from database
        MainDepartment mainDepartment = Optional.ofNullable(employee.getMainDepartment()).map(md -> mainDepartmentRepo.findById(md.getId()).orElseGet(MainDepartment::new)).orElseGet(MainDepartment::new);

        SubDepartment subDepartment = Optional.ofNullable(employee.getSubDepartment()).map(sd -> subDepartmentRepo.findById(sd.getId()).orElseGet(SubDepartment::new)).orElseGet(SubDepartment::new);

        SalaryAccountDetails salaryAccountDetails = Optional.ofNullable(employee.getSalaryAccountDetails()).map(sd -> salaryAccDetailsRepo.findById(sd.getId()).orElseGet(SalaryAccountDetails::new)).orElseGet(SalaryAccountDetails::new);

        List<DependentDetails> dependentDetails = dependentDetailsRepo.findByEmployee_Id(employee.getId()).orElse(Collections.emptyList());

        Designation designation = Optional.ofNullable(employee.getDesignation()).map(d -> designationRepo.findById(d.getId()).orElseGet(Designation::new)).orElseGet(Designation::new);

        Organization organization = Optional.ofNullable(employee.getOrganization()).map(o -> organizationRepo.findById(o.getId()).orElseGet(Organization::new)).orElseGet(Organization::new);

        Project project = Optional.ofNullable(employee.getProject()).map(p -> projectRepo.findById(p.getId()).orElseGet(Project::new)).orElseGet(Project::new);

        OrgHierarchy orgHierarchy = Optional.ofNullable(employee.getOrgHierarchy()).map(oh -> orgHierarchyRepo.findById(oh.getId()).orElseGet(OrgHierarchy::new)).orElseGet(OrgHierarchy::new);

        EmploymentStatus employmentStatus = Optional.ofNullable(employee.getEmploymentStatus()).map(es -> employmentStatusRepo.findById(es.getId()).orElseGet(EmploymentStatus::new)).orElseGet(EmploymentStatus::new);

        Category category = Optional.ofNullable(employee.getCategory()).map(c -> categoryRepo.findById(c.getId()).orElseGet(Category::new)).orElseGet(Category::new);

        // converting to response DTO

        MainDeptResDTO mainDeptResDTO = ModelConverter.convertToMainDeptResDTO(mainDepartment);
        SubDeptResDTO subDeptResDTO = ModelConverter.convertToSubDeptResDTO(subDepartment);
        SalaryAccDetailsResDTO salaryAccDetailsResDTO = ModelConverter.convertToSalAccResDTO(salaryAccountDetails);
        List<DependentDetailsResDTO> dependentDetailsResDTOS = ModelConverter.convertToDependentDetailsResDTOList(dependentDetails);
        DesignationResDTO designationResDTO = ModelConverter.convertToDegResDTO(designation);
        OrganizationResDTO organizationResDTO = ModelConverter.convertToOrgResDTO(organization);
        ProjectResDTO projectResDTO = ModelConverter.convertToProjectResDTO(project);
        OrgHierarchyResDTO orgHierarchyResDTO = ModelConverter.convertToOrgHierarchyResDTO(orgHierarchy);
        EmploymentStatusResDTO employmentStatusResDTO = ModelConverter.convertToEmploymentStatusResDTO(employmentStatus);
        CategoryResDTO categoryResDTO = ModelConverter.convertToCategoryResDTO(category);


        // fetching current company experience
        String currCompanyExp = calculateCurrCompanyExp(employee);

        // set all data in employee response DTO
        empResDTO = ModelConverter.convertToEmpResDTO(employee);
        empResDTO.setMainDeptResDTO(mainDeptResDTO);
        empResDTO.setSubDeptResDTO(subDeptResDTO);
        empResDTO.setSalaryAccDetailsResDTO(salaryAccDetailsResDTO);
        empResDTO.setDependentDetailsResDTOS(dependentDetailsResDTOS);
        empResDTO.setDesignationResDTO(designationResDTO);
        empResDTO.setOrganizationResDTO(organizationResDTO);
        empResDTO.setProjectResDTO(projectResDTO);
        empResDTO.setOrgHierarchyResDTO(orgHierarchyResDTO);
        empResDTO.setEmploymentStatusResDTO(employmentStatusResDTO);
        empResDTO.setCategoryResDTO(categoryResDTO);
        empResDTO.setExpWithCurrentCompany(currCompanyExp);

        return empResDTO;
    }


    public String calculateCurrCompanyExp(Employee employee) {
        LocalDate currentDate = LocalDate.now();

        int currCompanyYears = 0;
        int currCompanyMonths = 0;
        String currentCompanyExp = "";

        Period per = Period.between(employee.getDateOfJoining(), currentDate);
        currCompanyYears = per.getYears() + per.getMonths() / 12;
        currCompanyMonths = per.getMonths() % 12;

        currentCompanyExp = currCompanyYears + " Years " + currCompanyMonths + " Months";

        return currentCompanyExp;
    }

    public EmpHierarchyResDTO returningReportingMgrResponse(Employee employee) {
        EmpHierarchyResDTO reportingMgrEmpResDTO;
        Designation designation = Optional.ofNullable(employee.getDesignation()).map(d -> designationRepo.findById(d.getId()).orElseGet(Designation::new)).orElseGet(Designation::new);

        DesignationResDTO designationResDTO = ModelConverter.convertToDegResDTO(designation);

        reportingMgrEmpResDTO = ModelConverter.convertToReportingManagerResponseDTO(employee);
        reportingMgrEmpResDTO.setDesignationResDTO(designationResDTO);

        return reportingMgrEmpResDTO;
    }

    public List<Employee> fetchEmployeesUnderManager(String empCode) {
        return employeeRepo.findByReportTo(empCode);
    }

    public EmpAndUserResponse returnEmployeeAndUserResponse(Employee employee) throws ExecutionException, InterruptedException {
        CompletableFuture<EmpResDTO> empResFuture = CompletableFuture.supplyAsync(()->returningEmployeeResponse(employee));

        CompletableFuture<FileAndContentTypeBean> fileFuture = CompletableFuture.completedFuture(null);
        if (employee.getProfileImgDocId() != null) {
            fileFuture = documentOperations.openAndDownloadDocument(employee.getProfileImgDocId());
        }

        CompletableFuture<UserDTO> userFuture = CompletableFuture.supplyAsync(()-> employee.getUserId()!=null?userOperations.getUserByUserId(employee.getUserId()):null);

        EmpResDTO empResDTO = empResFuture.get();
        FileAndContentTypeBean fileAndContentTypeBean = fileFuture.get();
        UserDTO userDTO = userFuture.get();

        FileAndObjectTypeBean fileAndObjectTypeBean = new FileAndObjectTypeBean();
        fileAndObjectTypeBean.setEmpResDTO(empResDTO);
        fileAndObjectTypeBean.setFileAndContentTypeBean(fileAndContentTypeBean);

        EmpAndUserResponse empAndUserResponse = new EmpAndUserResponse();
        empAndUserResponse.setFileAndObjectTypeBean(fileAndObjectTypeBean);
        empAndUserResponse.setUserDTO(userDTO);

        return empAndUserResponse;
    }

    public CompletableFuture<Void> fetchTeamMembersRecursively(String empCode, List<EmpAndUserResponse> allTeamMembers, ExecutorService executorService) throws ExecutionException, InterruptedException {
        // Fetch direct reports for the given empCode
        List<Employee> directReports = fetchEmployeesUnderManager(empCode);


        if (directReports == null || directReports.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }

        // Convert to EmpAndUserResponse and add to the result list
        List<CompletableFuture<EmpAndUserResponse>> empUserFutures = directReports.stream()
                .map(emp-> CompletableFuture.supplyAsync(()-> {
                    try {
                        return returnEmployeeAndUserResponse(emp);
                    } catch (ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }, executorService)).toList();

        CompletableFuture<List<EmpAndUserResponse>> allEmpUserFutures = CompletableFuture.allOf(empUserFutures.toArray(new CompletableFuture[0]))
                .thenApply(emp -> empUserFutures.stream().map(CompletableFuture::join).toList());

        // Fetch sub-teams recursively using CompletableFuture for better performance
        List<CompletableFuture<Void>> recursiveFutures  = directReports.stream()
                .map(employee -> CompletableFuture.runAsync(() -> {
                    try {
                        fetchTeamMembersRecursively(employee.getEmpCode(), allTeamMembers, executorService).join();
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                },executorService)).toList();

        return CompletableFuture.allOf(allEmpUserFutures.thenAccept(allTeamMembers::addAll), CompletableFuture.allOf(recursiveFutures.toArray(new CompletableFuture[0])));
    }

}

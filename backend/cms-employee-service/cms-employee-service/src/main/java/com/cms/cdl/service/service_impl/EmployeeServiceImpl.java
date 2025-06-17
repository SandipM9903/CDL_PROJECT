package com.cms.cdl.service.service_impl;

import com.cms.cdl.beans.*;
import com.cms.cdl.constant_variables.ConstantVariables;
import com.cms.cdl.dto.document_dto.DocumentDTO;
import com.cms.cdl.dto.request_dto.EmpReqDTO;
import com.cms.cdl.dto.response_dto.EmpHierarchyResDTO;
import com.cms.cdl.dto.response_dto.EmpResDTO;
import com.cms.cdl.dto.response_dto.EmployeeResponseITDecDTO;
import com.cms.cdl.dto.user_dto.UserDTO;
import com.cms.cdl.model.Employee;
import com.cms.cdl.model.MainDepartment;
import com.cms.cdl.model.SubDepartment;
import com.cms.cdl.repo.*;
import com.cms.cdl.service.service_interface.EmployeeService;
import com.cms.cdl.utils.DocumentOperations;
import com.cms.cdl.utils.EmployeeCommonFunctions;
import com.cms.cdl.utils.ModelConverter;
import com.cms.cdl.utils.UserOperations;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    EmployeeRepo employeeRepo;
    @Autowired
    MainDepartmentRepo mainDepartmentRepo;
    @Autowired
    SubDepartmentRepo subDepartmentRepo;
    @Autowired
    DocumentOperations documentOperations;
    @Autowired
    EmployeeCommonFunctions employeeCommonFunctions;
    @Autowired
    ProjectRepo projectRepo;
    @Autowired
    OrganizationRepo organizationRepo;
    @Autowired
    DesignationRepo designationRepo;
    @Autowired
    BuHeadsRepo buHeadsRepo;
    @Autowired
    CategoryRepo categoryRepo;
    @Autowired
    ClassificationRepo classificationRepo;
    @Autowired
    EESubGroupRepo eeSubGroupRepo;
    @Autowired
    EmploymentStatusRepo employmentStatusRepo;
    @Autowired
    GenerationTypeRepo generationTypeRepo;
    @Autowired
    GradeRepo gradeRepo;
    @Autowired
    PayrollAreaTypeRepo payrollAreaTypeRepo;
    @Autowired
    OrgHierarchyRepo orgHierarchyRepo;
    @Autowired
    UserOperations userOperations;
    @Value("${myDocuments.API}")
    private String myDocsAPI;

    private static final int size = 40;

//    private static final int BATCH_SIZE = 10;


    @Override
    public EmpResDTO addEmployees(EmpReqDTO empReqDTO, MultipartFile empProfileImg) throws IOException {

        Employee employee = ModelConverter.convertToEmployee(empReqDTO);

        // Fetch and set related entities
        mainDepartmentRepo.findById(empReqDTO.getMainDeptId()).ifPresent(employee::setMainDepartment);
        subDepartmentRepo.findById(empReqDTO.getSubDeptId()).ifPresent(employee::setSubDepartment);
        projectRepo.findById(empReqDTO.getProjectId()).ifPresent(employee::setProject);
        organizationRepo.findById(empReqDTO.getOrgCode()).ifPresent(employee::setOrganization);
        designationRepo.findById(empReqDTO.getDesignationId()).ifPresent(employee::setDesignation);
        buHeadsRepo.findById(empReqDTO.getBuHeadId()).ifPresent(employee::setBuHeads);
        categoryRepo.findById(empReqDTO.getCategoryId()).ifPresent(employee::setCategory);
        classificationRepo.findById(empReqDTO.getClassificationId()).ifPresent(employee::setClassification);
        eeSubGroupRepo.findById(empReqDTO.getEeSubgroupId()).ifPresent(employee::setEeSubGroup);
        employmentStatusRepo.findById(empReqDTO.getEmploymentStatusId()).ifPresent(employee::setEmploymentStatus);
        generationTypeRepo.findById(empReqDTO.getGenerationTypeId()).ifPresent(employee::setGenerationType);
        gradeRepo.findById(empReqDTO.getGradeId()).ifPresent(employee::setGrade);
        payrollAreaTypeRepo.findById(empReqDTO.getPayrollAreaTypeId()).ifPresent(employee::setPayrollAreaType);
        orgHierarchyRepo.findById(empReqDTO.getOrgHierarchyId()).ifPresent(employee::setOrgHierarchy);


        // save employee in database without documentId
        Employee savedEmployee = employeeRepo.save(employee);
        Employee updatedEmployee = null;

        if (empProfileImg != null && !empProfileImg.isEmpty()) {
            DocumentDTO documentData = new DocumentDTO();
            documentData.setEmpCode(savedEmployee.getEmpCode());
            documentData.setEmpOrg(savedEmployee.getOrgHierarchy().getOrgHierarchy());
            documentData.setLocation("INDIA.KARNATAKA.BENGALURU.VASANTH NAGAR");

            List<DocumentDTO> documentDTO = documentOperations.uploadDocument(documentData, empProfileImg);
            savedEmployee.setProfileImgDocId(documentDTO.get(0).getDocId());
            // save employee in database with documentId
            updatedEmployee = employeeRepo.save(savedEmployee);
        }

        if (updatedEmployee != null) {
            return employeeCommonFunctions.returningEmployeeResponse(updatedEmployee);
        } else {
            return employeeCommonFunctions.returningEmployeeResponse(savedEmployee);
        }
    }

    //    @CacheEvict(value = "empCache", allEntries = true)
    @Override
    public EmpResDTO updateEmployee(long empId, EmpReqDTO empReqDTO, MultipartFile empProfileImg) throws IOException {

        // fetch the existing employee data by using employee ID
        Optional<Employee> existingEmpOpt = employeeRepo.findById(empId);

        // convert the employee request DTO to model employee object
        Employee updatedEmpData = ModelConverter.convertToEmployee(empReqDTO);

        if (existingEmpOpt.isPresent()) {
            Employee existingEmpData = existingEmpOpt.get();

            Optional.ofNullable(updatedEmpData.getEmpCode()).ifPresent(existingEmpData::setEmpCode);
            Optional.ofNullable(updatedEmpData.getFirstName()).ifPresent(existingEmpData::setFirstName);
            Optional.ofNullable(updatedEmpData.getLastName()).ifPresent(existingEmpData::setLastName);
            Optional.ofNullable(updatedEmpData.getFullNameAsAadhaar()).ifPresent(existingEmpData::setFullNameAsAadhaar);
            Optional.ofNullable(updatedEmpData.getAge()).ifPresent(existingEmpData::setAge);
            Optional.ofNullable(updatedEmpData.getEmailId()).ifPresent(existingEmpData::setEmailId);
            Optional.ofNullable(updatedEmpData.getPassword()).ifPresent(existingEmpData::setPassword);
            Optional.ofNullable(updatedEmpData.getRoles()).ifPresent(existingEmpData::setRoles);
            Optional.ofNullable(updatedEmpData.getReportingManager()).ifPresent(existingEmpData::setReportingManager);
            Optional.ofNullable(updatedEmpData.getReportTo()).ifPresent(existingEmpData::setReportTo);
            Optional.ofNullable(updatedEmpData.getReportingManagerEmailId()).ifPresent(existingEmpData::setReportingManagerEmailId);
            Optional.ofNullable(updatedEmpData.getDateOfJoining()).ifPresent(existingEmpData::setDateOfJoining);
            Optional.ofNullable(updatedEmpData.getDateOfBirth()).ifPresent(existingEmpData::setDateOfBirth);
            Optional.ofNullable(updatedEmpData.getPrimaryContactNo()).ifPresent(existingEmpData::setPrimaryContactNo);
            Optional.ofNullable(updatedEmpData.getSecondaryContactNo()).ifPresent(existingEmpData::setSecondaryContactNo);
            Optional.ofNullable(updatedEmpData.getEmergencyContactNo()).ifPresent(existingEmpData::setEmergencyContactNo);
            Optional.ofNullable(updatedEmpData.getEmergencyContactName()).ifPresent(existingEmpData::setEmergencyContactName);
            Optional.ofNullable(updatedEmpData.getRelationWithEmergencyContact()).ifPresent(existingEmpData::setRelationWithEmergencyContact);
            Optional.ofNullable(updatedEmpData.getPersonalEmailId()).ifPresent(existingEmpData::setPersonalEmailId);
            Optional.ofNullable(updatedEmpData.getPassportNumber()).ifPresent(existingEmpData::setPassportNumber);
            Optional.ofNullable(updatedEmpData.getAboutEmp()).ifPresent(existingEmpData::setAboutEmp);
            Optional.ofNullable(updatedEmpData.getMaritalStatus()).ifPresent(existingEmpData::setMaritalStatus);
            Optional.ofNullable(updatedEmpData.getGender()).ifPresent(existingEmpData::setGender);
            Optional.ofNullable(updatedEmpData.getBloodGroup()).ifPresent(existingEmpData::setBloodGroup);

            if (updatedEmpData.isStatus() != existingEmpData.isStatus()) {
                existingEmpData.setStatus(updatedEmpData.isStatus());
            }

            Optional.ofNullable(updatedEmpData.getProbationPeriod()).ifPresent(existingEmpData::setProbationPeriod);
            Optional.ofNullable(updatedEmpData.getDateOfConfirmation()).ifPresent(existingEmpData::setDateOfConfirmation);

            if (updatedEmpData.isResignation() != existingEmpData.isResignation()) {
                existingEmpData.setResignation(updatedEmpData.isResignation());
            }

            Optional.ofNullable(updatedEmpData.getDateOfLeaving()).ifPresent(existingEmpData::setDateOfLeaving);
            Optional.ofNullable(updatedEmpData.getNoticePeriod()).ifPresent(existingEmpData::setNoticePeriod);
            Optional.ofNullable(updatedEmpData.getNomineeName()).ifPresent(existingEmpData::setNomineeName);

            if (updatedEmpData.isPtApplicability() != existingEmpData.isPtApplicability()) {
                existingEmpData.setPtApplicability(updatedEmpData.isPtApplicability());
            }

            if (updatedEmpData.isMlwfApplicability() != existingEmpData.isMlwfApplicability()) {
                existingEmpData.setMlwfApplicability(updatedEmpData.isMlwfApplicability());
            }

            Optional.ofNullable(updatedEmpData.getGmcApplicability()).ifPresent(existingEmpData::setGmcApplicability);
            Optional.ofNullable(updatedEmpData.getGtlApplicability()).ifPresent(existingEmpData::setGtlApplicability);
            Optional.ofNullable(updatedEmpData.getGpaApplicability()).ifPresent(existingEmpData::setGpaApplicability);
            Optional.ofNullable(updatedEmpData.getWcApplicability()).ifPresent(existingEmpData::setWcApplicability);
            Optional.ofNullable(updatedEmpData.getHiringHr()).ifPresent(existingEmpData::setHiringHr);


            // fetch and set the related entities
            Optional.of(empReqDTO.getMainDeptId()).filter(id -> id != 0).flatMap(mainDepartmentRepo::findById).ifPresent(existingEmpData::setMainDepartment);
            Optional.of(empReqDTO.getSubDeptId()).filter(id -> id != 0).flatMap(subDepartmentRepo::findById).ifPresent(existingEmpData::setSubDepartment);
            Optional.of(empReqDTO.getProjectId()).filter(id -> id != 0).flatMap(projectRepo::findById).ifPresent(existingEmpData::setProject);
            Optional.of(empReqDTO.getOrgCode()).filter(id -> id != 0).flatMap(organizationRepo::findById).ifPresent(existingEmpData::setOrganization);
            Optional.of(empReqDTO.getDesignationId()).filter(id -> id != 0).flatMap(designationRepo::findById).ifPresent(existingEmpData::setDesignation);
            Optional.of(empReqDTO.getBuHeadId()).filter(id -> id != 0).flatMap(buHeadsRepo::findById).ifPresent(existingEmpData::setBuHeads);
            Optional.of(empReqDTO.getCategoryId()).filter(id -> id != 0).flatMap(categoryRepo::findById).ifPresent(existingEmpData::setCategory);
            Optional.of(empReqDTO.getClassificationId()).filter(id -> id != 0).flatMap(classificationRepo::findById).ifPresent(existingEmpData::setClassification);
            Optional.of(empReqDTO.getEeSubgroupId()).filter(id -> id != 0).flatMap(eeSubGroupRepo::findById).ifPresent(existingEmpData::setEeSubGroup);
            Optional.of(empReqDTO.getEmploymentStatusId()).filter(id -> id != 0).flatMap(employmentStatusRepo::findById).ifPresent(existingEmpData::setEmploymentStatus);
            Optional.of(empReqDTO.getGenerationTypeId()).filter(id -> id != 0).flatMap(generationTypeRepo::findById).ifPresent(existingEmpData::setGenerationType);
            Optional.of(empReqDTO.getGradeId()).filter(id -> id != 0).flatMap(gradeRepo::findById).ifPresent(existingEmpData::setGrade);
            Optional.of(empReqDTO.getPayrollAreaTypeId()).filter(id -> id != 0).flatMap(payrollAreaTypeRepo::findById).ifPresent(existingEmpData::setPayrollAreaType);
            Optional.of(empReqDTO.getOrgHierarchyId()).filter(id -> id != 0).flatMap(orgHierarchyRepo::findById).ifPresent(existingEmpData::setOrgHierarchy);


            // Handle profile image update
            if (empProfileImg != null && !empProfileImg.isEmpty()) {
                // set the document data
                DocumentDTO documentData = new DocumentDTO();
                documentData.setEmpCode(updatedEmpData.getEmpCode());
                documentData.setEmpOrg(updatedEmpData.getOrgHierarchy().getOrgHierarchy());
                documentData.setLocation("INDIA.KARNATAKA.BENGALURU.VASANTH NAGAR");

                DocumentDTO documentDTO = documentOperations.updateDocument(documentData, existingEmpData.getProfileImgDocId(), empProfileImg, "Employee");

                existingEmpData.setProfileImgDocId(documentDTO.getDocId());
            }

            // Save the updated employee in the database
            Employee savedUpdatedEmployee = employeeRepo.save(existingEmpData);

            return employeeCommonFunctions.returningEmployeeResponse(savedUpdatedEmployee);
        } else {
            return null;
        }
    }

    //    @CacheEvict(value = "empCache", allEntries = true)
    @Override
    public boolean updateProfileImage(long empId, MultipartFile empProfileImg) throws IOException {
        Optional<Employee> optionalEmp = employeeRepo.findById(empId);

        if (optionalEmp.isPresent()) {
            Employee employee = optionalEmp.get();

            // fetch the user details
            UserDTO userDTO = userOperations.getUserByUserId(employee.getUserId());

            // get the location
            String country = userDTO.getLocationDTO().getCountry();
            String state = userDTO.getLocationDTO().getState();
            String city = userDTO.getLocationDTO().getCity();
            String locationName = userDTO.getLocationDTO().getLocationName();
            String district = userDTO.getLocationDTO().getDistrict();

            String location = country + "." + district + "." + state + "." + city + "." + locationName;

            // set the document data
            DocumentDTO docData = new DocumentDTO();
            docData.setEmpCode(employee.getEmpCode());
            docData.setEmpOrg(employee.getOrgHierarchy().getOrgHierarchy());
            docData.setLocation(location);

            // fetch the existing profile image docId if available
            Long docId = employee.getProfileImgDocId();

            // create the document object for creating or updating the profile image and receive data in that object
            DocumentDTO documentDTO = new DocumentDTO();

            if (docId != null) {
                documentDTO = documentOperations.updateDocument(docData, docId, empProfileImg, "CMS Employee");
            } else {
                documentDTO = documentOperations.uploadDocument(docData, empProfileImg).get(0);
            }

            // set the profile image docId in the employee
            employee.setProfileImgDocId(documentDTO.getDocId());

            // save the docId in the employee table
            employeeRepo.save(employee);

            return true;
        } else {
            return false;
        }

    }


    @Override
    public boolean deleteEmployee(long empId) {
        Optional<Employee> employee = employeeRepo.findById(empId);

        // delete Document from DMS and employee
        if (employee.isPresent()) {
            Employee emp = employee.get();

            if (emp.getProfileImgDocId() != null) {
                boolean docDeleted = documentOperations.deleteDocument(emp.getProfileImgDocId());
                if (!docDeleted) {
                    return false;
                }
            }

            // Delete the employee
            employeeRepo.deleteById(empId);
            return true;
        }

        // if employee not found
        return false;

    }

    @Cacheable(value = "empIdCache", key = "#empId")
    @Override
    public EmpAndUserResponse getEmployeeByEmployeeId(long empId) throws ExecutionException, InterruptedException {
        Optional<Employee> employee = employeeRepo.findById(empId);

        EmpAndUserResponse empAndUserResponse = new EmpAndUserResponse();
        FileAndObjectTypeBean fileAndObjectTypeBean = new FileAndObjectTypeBean();

        EmpResDTO empResDTO;
        UserDTO userDTO;

        if (employee.isPresent()) {
            Employee emp = employee.get();
            FileAndContentTypeBean fileAndContentTypeBean = null;
            empResDTO = employeeCommonFunctions.returningEmployeeResponse(emp);
            Long profileImgDocId = emp.getProfileImgDocId();
            if (profileImgDocId != null) {
                CompletableFuture<FileAndContentTypeBean> futureResult = documentOperations.openAndDownloadDocument(emp.getProfileImgDocId());
                fileAndContentTypeBean = futureResult.get();
            }

            // fetch the userDTO
            if (emp.getUserId() != null) {
                userDTO = userOperations.getUserByUserId(emp.getUserId());
            } else {
                userDTO = null;
            }

            // set the values of file and emp response
            fileAndObjectTypeBean.setEmpResDTO(empResDTO);
            fileAndObjectTypeBean.setFileAndContentTypeBean(fileAndContentTypeBean);

            empAndUserResponse.setFileAndObjectTypeBean(fileAndObjectTypeBean);
            empAndUserResponse.setUserDTO(userDTO);

            return empAndUserResponse;
        } else {
            return null;
        }
    }

    @Cacheable(value = "empCodeCache", key = "#empCode")
    @Override
    public EmpAndUserResponse getEmployeeByEmployeeCode(String empCode) throws ExecutionException, InterruptedException {
        Employee employee = employeeRepo.findByEmpCode(empCode);
        EmpResDTO empResDTO;
        if (employee != null) {
            empResDTO = employeeCommonFunctions.returningEmployeeResponse(employee);
            CompletableFuture<FileAndContentTypeBean> fileFuture = CompletableFuture.supplyAsync(() -> {
                if (employee.getProfileImgDocId() != null) {
                    return documentOperations.fetchDocument(employee.getProfileImgDocId());
                }
                return null;
            });
            CompletableFuture<UserDTO> userFuture = CompletableFuture.supplyAsync(() -> {
                if (employee.getUserId() != null) {
                    return userOperations.getUserByUserId(employee.getUserId());
                }
                return null;
            });
            return fileFuture.thenCombine(userFuture, (fileAndContentTypeBean, userDTO) -> {
                EmpAndUserResponse empAndUserResponse = new EmpAndUserResponse();
                empAndUserResponse.setFileAndObjectTypeBean(new FileAndObjectTypeBean(fileAndContentTypeBean, empResDTO));
                empAndUserResponse.setUserDTO(userDTO);
                return empAndUserResponse;
            }).get();
        } else {
            return null;
        }
    }

    @Cacheable(value = "empEmailCache", key = "#emailId")
    @Override
    public EmpAndUserResponse getEmployeeByEmailId(String emailId) throws ExecutionException, InterruptedException {
        Optional<Employee> employee = employeeRepo.findByEmailId(emailId);

        EmpAndUserResponse empAndUserResponse = new EmpAndUserResponse();
        FileAndObjectTypeBean fileAndObjectTypeBean = new FileAndObjectTypeBean();
        FileAndContentTypeBean fileAndContentTypeBean = null;

        EmpResDTO empResDTO;
        UserDTO userDTO;

        if (employee.isPresent()) {
            Employee emp = employee.get();

            empResDTO = employeeCommonFunctions.returningEmployeeResponse(emp);

            Long profileImgDocId = emp.getProfileImgDocId();

            if (profileImgDocId != null) {
                CompletableFuture<FileAndContentTypeBean> futureResult = documentOperations.openAndDownloadDocument(emp.getProfileImgDocId());
                fileAndContentTypeBean = futureResult.get();
            }

            // fetch the userDTO
            if (emp.getUserId() != null) {
                userDTO = userOperations.getUserByUserId(emp.getUserId());
            } else {
                userDTO = null;
            }

            // set the values of file and emp response
            fileAndObjectTypeBean.setEmpResDTO(empResDTO);
            fileAndObjectTypeBean.setFileAndContentTypeBean(fileAndContentTypeBean);

            empAndUserResponse.setFileAndObjectTypeBean(fileAndObjectTypeBean);
            empAndUserResponse.setUserDTO(userDTO);

            return empAndUserResponse;

        } else {
            return null;
        }
    }

    @Cacheable(value = "empPageCache", key = "#page")
    @Override
    public List<FileAndObjectTypeBean> getAllEmployeesWithPagination(int page) throws ExecutionException, InterruptedException {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Employee> employeeList = employeeRepo.findAll(pageRequest);
        EmpResDTO empResDTO;
        List<FileAndObjectTypeBean> allEmployees = new ArrayList<>();

        for (Employee employee : employeeList.getContent()) {
            empResDTO = employeeCommonFunctions.returningEmployeeResponse(employee);
            Long profileImgDocId = employee.getProfileImgDocId();
            FileAndContentTypeBean fileAndContentTypeBean = null;

            if (profileImgDocId != null) {
                CompletableFuture<FileAndContentTypeBean> futureResult = documentOperations.openAndDownloadDocument(profileImgDocId);
                fileAndContentTypeBean = futureResult.get();
            }
            FileAndObjectTypeBean fileAndObjectTypeBean = new FileAndObjectTypeBean();
            fileAndObjectTypeBean.setEmpResDTO(empResDTO);
            fileAndObjectTypeBean.setFileAndContentTypeBean(fileAndContentTypeBean);
            allEmployees.add(fileAndObjectTypeBean);
        }
        return allEmployees;
    }

    @Override
    public List<FileAndObjectTypeBean> getAllEmployees() throws ExecutionException, InterruptedException {
        List<Employee> employeeList = employeeRepo.findAll();
        EmpResDTO empResDTO;
        List<FileAndObjectTypeBean> allEmployees = new ArrayList<>();

        for (Employee employee : employeeList) {
            FileAndContentTypeBean fileAndContentTypeBean = null;
            empResDTO = employeeCommonFunctions.returningEmployeeResponse(employee);
            Long profileImgDocId = employee.getProfileImgDocId();

            if (profileImgDocId != null) {
                CompletableFuture<FileAndContentTypeBean> futureResult = documentOperations.openAndDownloadDocument(profileImgDocId);
                fileAndContentTypeBean = futureResult.get();
            }
            FileAndObjectTypeBean fileAndObjectTypeBean = new FileAndObjectTypeBean();
            fileAndObjectTypeBean.setEmpResDTO(empResDTO);
            fileAndObjectTypeBean.setFileAndContentTypeBean(fileAndContentTypeBean);
            allEmployees.add(fileAndObjectTypeBean);
        }
        return allEmployees;
    }

    @Override
    public List<FileAndObjectTypeBean> getBirthdayWishesData() throws ExecutionException, InterruptedException {
        List<Employee> employees = employeeRepo.findBirthdays();

        List<FileAndObjectTypeBean> birthdayData = new ArrayList<>();
        FileAndContentTypeBean fileAndContentTypeBean = null;

        EmpResDTO empResDTO;

        if (employees != null) {
            for (Employee emp : employees) {

                empResDTO = employeeCommonFunctions.returningEmployeeResponse(emp);

                Long profileImgDocId = emp.getProfileImgDocId();

                if (profileImgDocId != null) {
                    CompletableFuture<FileAndContentTypeBean> futureResult = documentOperations.openAndDownloadDocument(profileImgDocId);
                    fileAndContentTypeBean = futureResult.get();
                }
                FileAndObjectTypeBean fileAndObjectTypeBean = new FileAndObjectTypeBean();

                fileAndObjectTypeBean.setEmpResDTO(empResDTO);
                fileAndObjectTypeBean.setFileAndContentTypeBean(fileAndContentTypeBean);

                birthdayData.add(fileAndObjectTypeBean);
            }

            return birthdayData;
        } else {
            return null;
        }
    }

    @Override
    public List<FileAndObjectTypeBean> getWorkAnniversaryData() throws ExecutionException, InterruptedException {
        List<Employee> employees = employeeRepo.findWorkAnniversary();

        List<FileAndObjectTypeBean> workAnniversary = new ArrayList<>();
        FileAndContentTypeBean fileAndContentTypeBean = null;

        LocalDate currentDate = LocalDate.now();

        EmpResDTO empResDTO;

        if (employees != null) {
            for (Employee emp : employees) {

                int yearsCompleted = Period.between(emp.getDateOfJoining(), currentDate).getYears();

                empResDTO = employeeCommonFunctions.returningEmployeeResponse(emp);
                empResDTO.setYearsCompleted(yearsCompleted);

                Long profileImgDocId = emp.getProfileImgDocId();

                if (profileImgDocId != null) {
                    CompletableFuture<FileAndContentTypeBean> futureResult = documentOperations.openAndDownloadDocument(profileImgDocId);
                    fileAndContentTypeBean = futureResult.get();
                }

                FileAndObjectTypeBean fileAndObjectTypeBean = new FileAndObjectTypeBean();

                fileAndObjectTypeBean.setEmpResDTO(empResDTO);
                fileAndObjectTypeBean.setFileAndContentTypeBean(fileAndContentTypeBean);

                workAnniversary.add(fileAndObjectTypeBean);
            }
            return workAnniversary;
        }
        return null;
    }

    @Override
    public List<DocumentDTO> getMyDocuments(long empId) {
        return documentOperations.getMyDocuments(empId);
    }

    @Override
    public FileAndContentTypeBean openAndDownloadMyDocuments(long docId) throws ExecutionException, InterruptedException {
        CompletableFuture<FileAndContentTypeBean> futureResult = documentOperations.openAndDownloadDocument(docId);
        return futureResult.get();
    }

    @Cacheable(value = "empLocCache", key = "#locationId +'-'+#page")
    @Override
    @Transactional
    public List<EmpAndUserResponse> getEmployeesLocationBased(Long locationId, int page) throws ExecutionException, InterruptedException {
        if (locationId == null) {
            return Collections.emptyList();
        }
        CompletableFuture<List<UserDTO>> userFuture = CompletableFuture.supplyAsync(() -> userOperations.getUserByLocation(locationId, page));
        CompletableFuture<List<Employee>> employeeFuture = userFuture.thenApply(userDTOList -> {
            List<Long> userIds = userDTOList.stream().map(UserDTO::getUserId).toList();
            return employeeRepo.findEmployeesByUserIdsAndStatus(userIds);
        });
        List<UserDTO> userDTOList = userFuture.get();
        List<Employee> employeeList = employeeFuture.get();
        Map<Long, UserDTO> userDTOMap = userDTOList.stream().collect(Collectors.toMap(UserDTO::getUserId, Function.identity()));
        EmpResDTO empResDTO = null;
        UserDTO userDTO = null;
        List<EmpAndUserResponse> locationBasedEmp = new ArrayList<>();
        for (Employee emp : employeeList) {
            empResDTO = employeeCommonFunctions.returningEmployeeResponse(emp);
            Long profileImgDocId = emp.getProfileImgDocId();
            FileAndContentTypeBean fileAndContentTypeBean = null;
            if (profileImgDocId != null) {
                CompletableFuture<FileAndContentTypeBean> futureResult = documentOperations.openAndDownloadDocument(emp.getProfileImgDocId());
                fileAndContentTypeBean = futureResult.get();
            }
            FileAndObjectTypeBean fileAndObjectTypeBean = new FileAndObjectTypeBean();
            EmpAndUserResponse empAndUserResponse = new EmpAndUserResponse();
            // set the values of file and emp response
            fileAndObjectTypeBean.setEmpResDTO(empResDTO);
            fileAndObjectTypeBean.setFileAndContentTypeBean(fileAndContentTypeBean);
            empAndUserResponse.setFileAndObjectTypeBean(fileAndObjectTypeBean);
            // Get the matching UserDTO for the employee's userId
            userDTO = userDTOMap.get(emp.getUserId());
            empAndUserResponse.setUserDTO(userDTO);
            locationBasedEmp.add(empAndUserResponse);
        }
        return locationBasedEmp;
    }

    @Override
    @Cacheable(value = "hrHiredEmp", key = "'hiringHR_' + #empCode")
    public List<EmpAndUserResponse> getHRHiredEmployees(String empCode) throws ExecutionException, InterruptedException {
        List<Employee> employees = employeeRepo.findByHiringHr(empCode);
        if (employees.isEmpty()) {
            return null;
        }

        // Custom ThreadPoolExecutor for controlling concurrency
        int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;
        int maximumPoolSize = corePoolSize * 2;
        long keepAliveTime = 60L;
        TimeUnit unit = TimeUnit.SECONDS;
        int queueCapacity = 1000;

        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(queueCapacity);
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new ThreadPoolExecutor.CallerRunsPolicy());

        final int BATCH_SIZE = 100;

        List<EmpAndUserResponse> resultList = new ArrayList<>();

        // Process employees in batches
        for (int i = 0; i < employees.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, employees.size());
            List<Employee> batch = employees.subList(i, end);

            // Process each batch asynchronously
            List<CompletableFuture<EmpAndUserResponse>> batchFutures = batch.stream()
                    .map(employee -> {
                        EmpResDTO empResDTO = employeeCommonFunctions.returningEmployeeResponse(employee);
                        CompletableFuture<FileAndContentTypeBean> fileFuture = CompletableFuture.supplyAsync(() -> {
                            if (employee.getProfileImgDocId() != null) {
                                return documentOperations.fetchDocument(employee.getProfileImgDocId());
                            }
                            return null;
                        }, executorService);
                        CompletableFuture<UserDTO> userFuture = CompletableFuture.supplyAsync(() -> {
                            if (employee.getUserId() != null) {
                                return userOperations.getUserByUserId(employee.getUserId());
                            }
                            return null;
                        }, executorService);
                        return fileFuture.thenCombine(userFuture, (fileAndContentTypeBean, userDTO) -> {
                            EmpAndUserResponse empAndUserResponse = new EmpAndUserResponse();
                            empAndUserResponse.setFileAndObjectTypeBean(new FileAndObjectTypeBean(fileAndContentTypeBean, empResDTO));
                            empAndUserResponse.setUserDTO(userDTO);
                            return empAndUserResponse;
                        });
                    }).toList();
            // Wait for all futures in the batch to complete
            CompletableFuture.allOf(batchFutures.toArray(new CompletableFuture[0])).join();
            // Collect the results from the current batch
            List<EmpAndUserResponse> batchResults = batchFutures.stream()
                    .map(CompletableFuture::join)
                    .toList();
            resultList.addAll(batchResults);
        }
        return resultList;
    }

    // for help_ticketing
    public List<DepartmentBean> getAllDepartments() {
        List<DepartmentBean> departmentsBeansList = new ArrayList<>();
        List<MainDepartment> mainDepartmentsList = mainDepartmentRepo.findAll();
        for (MainDepartment mainDepartment : mainDepartmentsList) {
            DepartmentBean departmentBean = new DepartmentBean();
            departmentBean.setMainDepartment(mainDepartment.getMainDeptName());
            // fetching sub departments and linked to respective main department
            List<SubDepartment> subDepartmentsList = subDepartmentRepo.getAllSubDepartments(mainDepartment.getId());
            String subDept = "";
            for (SubDepartment subDepartment : subDepartmentsList) {
                subDept += subDepartment.getSubDeptName() + ",";
            }
            departmentBean.setSubDepartment(subDept.substring(0, subDept.length() - 1));
            departmentsBeansList.add(departmentBean);
        }
        return departmentsBeansList;
    }

    @Override
    public List<EmpAndUserResponse> fetchEmployeesBasedOnRepMgrEmpCode(String empCode) throws ExecutionException, InterruptedException {
        List<Employee> employeeList = employeeRepo.findByReportTo(empCode);
        List<EmpAndUserResponse> empAndUserResponseList = new ArrayList<>();
        EmpResDTO empResDTO;
        UserDTO userDTO;

        for (Employee employee : employeeList) {
            empResDTO = employeeCommonFunctions.returningEmployeeResponse(employee);
            FileAndContentTypeBean fileAndContentTypeBean = null;

            Long profileImgDocId = employee.getProfileImgDocId();

            if (profileImgDocId != null) {
                CompletableFuture<FileAndContentTypeBean> futureResult = documentOperations.openAndDownloadDocument(employee.getProfileImgDocId());
                fileAndContentTypeBean = futureResult.get();
            }

            // fetch the userDTO
            if (employee.getUserId() != null) {
                userDTO = userOperations.getUserByUserId(employee.getUserId());
            } else {
                userDTO = null;
            }
            FileAndObjectTypeBean fileAndObjectTypeBean = new FileAndObjectTypeBean();
            // set the values of file and emp response
            fileAndObjectTypeBean.setEmpResDTO(empResDTO);
            fileAndObjectTypeBean.setFileAndContentTypeBean(fileAndContentTypeBean);

            EmpAndUserResponse empAndUserResponse = new EmpAndUserResponse();

            empAndUserResponse.setFileAndObjectTypeBean(fileAndObjectTypeBean);
            empAndUserResponse.setUserDTO(userDTO);
            empAndUserResponseList.add(empAndUserResponse);

        }
        return empAndUserResponseList;
    }

    @Override
    public List<EmployeeResponseITDecDTO> getAllEmployeeForAdmin() {
        List<Employee> employeeList = employeeRepo.findAll();
        return ModelConverter.convertToEmpResDTOList(employeeList);
    }

    @Override
    public Object fetchPasswordForDocument(String empCode) {
        Map<String, Object> map = new HashMap<>();
        Employee employee = employeeRepo.findByEmpCode(empCode);
        if (employee != null) {
            String password;
            String dob = (employee.getDateOfBirth() != null) ? employee.getDateOfBirth().toString() : null;
            String primaryContactNo = employee.getPrimaryContactNo();

            // Get first 4 chars of empCode
            String firstFourChars = empCode.length() >= 4 ? empCode.substring(0, 4) : empCode;

            String secondPart;

            if (dob != null && dob.length() >= 4) {
                // Use year from DOB
                secondPart = dob.substring(0, 4);
            } else if (primaryContactNo != null && primaryContactNo.length() >= 4) {
                // Use last 4 digits of contact number
                secondPart = primaryContactNo.substring(primaryContactNo.length() - 4);
            } else {
                // Fallback if both are missing
                secondPart = "0000";
            }

            password = firstFourChars + secondPart;

            map.put("passwd", password);
            map.put("empCode", empCode);
            map.put("hint", "Password is in combination of First Four digit of employee code and " + (dob != null ? "Year of birth" : primaryContactNo != null ? "Last 4 digits of primary contact" : "0000"));
        }
        return map;
    }

    @Override
    public Map<String, Object> fetchEmployeesUnderManager(String empCode) throws ExecutionException, InterruptedException {
        List<Employee> empTeamList = employeeRepo.findByReportTo(empCode);
        List<FileAndEmpTypeBean> teamList = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();

        List<CompletableFuture<FileAndContentTypeBean>> teamImageFutures = new ArrayList<>();

        for (Employee employee : empTeamList) {
            EmpHierarchyResDTO empHierarchyResDTOForTeam = employeeCommonFunctions.returningReportingMgrResponse(employee);
            Long profileImgDocId = employee.getProfileImgDocId();

            CompletableFuture<FileAndContentTypeBean> futureResult = CompletableFuture.supplyAsync(() -> {
                if (profileImgDocId != null) {
                    try {
                        return documentOperations.openAndDownloadDocument(profileImgDocId).get();
                    } catch (Exception e) {
                        // Consider adding a logger to record this error
                        return null;
                    }
                }
                return null;
            });

            teamImageFutures.add(futureResult);

            FileAndEmpTypeBean fileAndEmpTypeBean = new FileAndEmpTypeBean();
            fileAndEmpTypeBean.setEmpHierarchyResDTO(empHierarchyResDTOForTeam);
            teamList.add(fileAndEmpTypeBean);
        }

        // Wait for all async tasks to complete
        CompletableFuture<Void> allOf = CompletableFuture.allOf(teamImageFutures.toArray(new CompletableFuture[0]));
        allOf.get();

        // Set downloaded images into the corresponding team beans
        for (int i = 0; i < teamList.size(); i++) {
            FileAndEmpTypeBean teamMemberBean = teamList.get(i);
            FileAndContentTypeBean image = teamImageFutures.get(i).get(); // Can still throw, so catch at call site if needed
            teamMemberBean.setFileAndContentTypeBean(image);
        }

        response.put("teamList", teamList);
        return response;
    }

    @Override
    @Cacheable(value = ConstantVariables.empHierarchyAllTeam, key = "#empCode")
    public List<EmpAndUserResponse> fetchAllTeamMembers(String empCode) throws ExecutionException, InterruptedException {
        int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;
        int maximumPoolSize = corePoolSize * 2;
        long keepAliveTime = 60L;
        TimeUnit unit = TimeUnit.SECONDS;
        int queueCapacity = 1000;

        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(queueCapacity);
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new ThreadPoolExecutor.CallerRunsPolicy());

        try {
            List<EmpAndUserResponse> allTeamMembers = Collections.synchronizedList(new ArrayList<>());
            employeeCommonFunctions.fetchTeamMembersRecursively(empCode, allTeamMembers, executorService).get();
            return new ArrayList<>(allTeamMembers);
        } finally {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException ex) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

}

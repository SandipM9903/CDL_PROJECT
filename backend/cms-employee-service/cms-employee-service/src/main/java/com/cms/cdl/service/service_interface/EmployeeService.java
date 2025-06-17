package com.cms.cdl.service.service_interface;

import com.cms.cdl.beans.*;
import com.cms.cdl.dto.document_dto.DocumentDTO;
import com.cms.cdl.dto.request_dto.EmpReqDTO;
import com.cms.cdl.dto.response_dto.EmpResDTO;
import com.cms.cdl.dto.response_dto.EmployeeResponseITDecDTO;
import com.cms.cdl.model.Employee;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface EmployeeService {
    EmpResDTO addEmployees(EmpReqDTO empReqDTO, MultipartFile empProfileImg) throws IOException;
    EmpResDTO updateEmployee(long empId, EmpReqDTO empReqDTO, MultipartFile profileImage) throws IOException;
    boolean updateProfileImage(long empId, MultipartFile empProfileImg) throws IOException;
    boolean deleteEmployee(long empId);
    EmpAndUserResponse getEmployeeByEmployeeId(long empId) throws ExecutionException, InterruptedException;
    EmpAndUserResponse getEmployeeByEmployeeCode(String empCode) throws ExecutionException, InterruptedException;
    EmpAndUserResponse getEmployeeByEmailId(String emailId) throws ExecutionException, InterruptedException;
    List<FileAndObjectTypeBean> getAllEmployeesWithPagination(int page) throws ExecutionException, InterruptedException;
    List<FileAndObjectTypeBean> getAllEmployees() throws ExecutionException, InterruptedException;

    List<FileAndObjectTypeBean> getBirthdayWishesData() throws ExecutionException, InterruptedException;
    List<FileAndObjectTypeBean> getWorkAnniversaryData() throws ExecutionException, InterruptedException;
    List<DocumentDTO> getMyDocuments(long empId);
    FileAndContentTypeBean openAndDownloadMyDocuments(long docId) throws ExecutionException, InterruptedException;
    List<EmpAndUserResponse> getEmployeesLocationBased(Long locationId, int page) throws ExecutionException, InterruptedException;
    List<EmpAndUserResponse> getHRHiredEmployees(String empCode) throws ExecutionException, InterruptedException;
    List<DepartmentBean> getAllDepartments();
    List<EmpAndUserResponse> fetchEmployeesBasedOnRepMgrEmpCode(String empCode) throws ExecutionException, InterruptedException;
    List<EmployeeResponseITDecDTO> getAllEmployeeForAdmin();
    Object fetchPasswordForDocument(String empCode);
    Map<String, Object> fetchEmployeesUnderManager(String empCode) throws ExecutionException, InterruptedException;
    List<EmpAndUserResponse> fetchAllTeamMembers(String empCode) throws ExecutionException, InterruptedException;
}

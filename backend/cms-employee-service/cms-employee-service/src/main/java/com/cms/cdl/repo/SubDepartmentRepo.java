package com.cms.cdl.repo;

import com.cms.cdl.model.SubDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubDepartmentRepo extends JpaRepository<SubDepartment, Long> {
    @Query(value = "select * from sub_department where main_dept_id = :mainDeptId", nativeQuery = true)
    List<SubDepartment> getAllSubDepartments(long mainDeptId);
}

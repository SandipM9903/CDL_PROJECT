package com.cms.cdl.repo;

import com.cms.cdl.model.MainDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainDepartmentRepo extends JpaRepository<MainDepartment, Long> {
}

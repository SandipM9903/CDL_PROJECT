package com.cms.cdl.repo;

import com.cms.cdl.model.EmploymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmploymentStatusRepo extends JpaRepository<EmploymentStatus, Long> {
}

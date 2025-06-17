package com.cms.cdl.repo;

import com.cms.cdl.model.SalaryAccountDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalaryAccDetailsRepo extends JpaRepository<SalaryAccountDetails, Long> {
    Optional<SalaryAccountDetails> findByEmployee_Id(long empId);
}

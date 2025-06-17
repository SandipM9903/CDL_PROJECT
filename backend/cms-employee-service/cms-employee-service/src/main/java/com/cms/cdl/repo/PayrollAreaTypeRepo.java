package com.cms.cdl.repo;

import com.cms.cdl.model.PayrollAreaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayrollAreaTypeRepo extends JpaRepository<PayrollAreaType,Long> {
}

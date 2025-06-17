package com.cms.cdl.repo;

import com.cms.cdl.model.DependentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DependentDetailsRepo extends JpaRepository<DependentDetails, Long> {
    Optional<List<DependentDetails>> findByEmployee_Id(long empId);
}

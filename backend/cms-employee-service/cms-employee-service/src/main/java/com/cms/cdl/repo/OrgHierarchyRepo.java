package com.cms.cdl.repo;

import com.cms.cdl.model.OrgHierarchy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgHierarchyRepo extends JpaRepository<OrgHierarchy, Long> {
}

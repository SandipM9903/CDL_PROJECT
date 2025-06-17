package com.cms.cdl.repo;

import com.cms.cdl.model.EESubGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EESubGroupRepo extends JpaRepository<EESubGroup, Long> {
}

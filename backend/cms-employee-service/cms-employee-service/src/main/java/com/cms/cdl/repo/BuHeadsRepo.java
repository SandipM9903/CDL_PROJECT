package com.cms.cdl.repo;

import com.cms.cdl.model.BuHeads;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuHeadsRepo extends JpaRepository<BuHeads, Long> {
}

package com.cms.cdl.repo;

import com.cms.cdl.model.Category;
import com.cms.cdl.model.Classification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassificationRepo extends JpaRepository<Classification, Long> {
}

package com.cms.cdl.repo;

import com.cms.cdl.model.GenerationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenerationTypeRepo extends JpaRepository<GenerationType, Long> {
}

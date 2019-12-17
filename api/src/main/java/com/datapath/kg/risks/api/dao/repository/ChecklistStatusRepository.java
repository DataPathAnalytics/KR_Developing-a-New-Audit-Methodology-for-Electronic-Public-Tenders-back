package com.datapath.kg.risks.api.dao.repository;

import com.datapath.kg.risks.api.dao.entity.ChecklistStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecklistStatusRepository extends JpaRepository<ChecklistStatusEntity, Integer> {
}

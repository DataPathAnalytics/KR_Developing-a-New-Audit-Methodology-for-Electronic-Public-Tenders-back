package com.datapath.kg.risks.api.dao.repository;

import com.datapath.kg.risks.api.dao.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Integer> {
}

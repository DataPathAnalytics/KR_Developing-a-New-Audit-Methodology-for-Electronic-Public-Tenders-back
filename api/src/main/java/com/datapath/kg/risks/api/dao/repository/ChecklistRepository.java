package com.datapath.kg.risks.api.dao.repository;

import com.datapath.kg.risks.api.dao.entity.ChecklistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChecklistRepository extends JpaRepository<ChecklistEntity, Integer>, JpaSpecificationExecutor<ChecklistEntity> {

    ChecklistEntity getById(Integer id);

    @Query(value = "SELECT * FROM checklist WHERE audit_name ILIKE CONCAT(:value, '%') LIMIT 10", nativeQuery = true)
    List<ChecklistEntity> searchByAuditName(@Param("value") String value);

    @Query(value = "SELECT * FROM checklist WHERE audit_name ILIKE CONCAT(:value, '%') AND status_id = 2 LIMIT 10", nativeQuery = true)
    List<ChecklistEntity> searchByAuditNameForAdmin(@Param("value") String value);

    @Query(value = "SELECT id FROM checklist WHERE auditor_id = :auditorId AND status_id = 1", nativeQuery = true)
    List<Integer> getActiveChecklistsByAuditor(@Param("auditorId") Integer auditorId);

    @Query(value = "SELECT count(id) FROM checklist WHERE auditor_id = :auditorId AND status_id = 2", nativeQuery = true)
    Integer getCompletedChecklistCount(@Param("auditorId") Integer auditorId);

    @Query(value = "SELECT * FROM checklist WHERE buyer_id = :buyerId AND status_id = 2 AND tender_id IS NOT NULL", nativeQuery = true)
    List<ChecklistEntity> getTendersChecklistsByBuyerId(@Param("buyerId") Integer buyerId);
}

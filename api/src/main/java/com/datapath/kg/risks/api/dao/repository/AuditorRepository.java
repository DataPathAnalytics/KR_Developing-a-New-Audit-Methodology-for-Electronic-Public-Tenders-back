package com.datapath.kg.risks.api.dao.repository;

import com.datapath.kg.risks.api.dao.entity.AuditorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuditorRepository extends JpaRepository<AuditorEntity, Integer> {

    AuditorEntity findByEmail(String email);

    @Query(value = "select distinct a.* from checklist c join auditor a on c.auditor_id = a.id WHERE a.name ILIKE CONCAT(:value, '%') LIMIT 10", nativeQuery = true)
    List<AuditorEntity> searchByAuditorName(@Param("value") String value);

    @Query(value = "SELECT distinct a.* FROM checklist c JOIN auditor a ON c.auditor_id = a.id WHERE a.name ILIKE CONCAT(:value, '%') AND c.status_id = 2 LIMIT 10", nativeQuery = true)
    List<AuditorEntity> searchByAuditorNameForAdmin(@Param("value") String value);

    @Query(value = "SELECT * FROM auditor WHERE account_locked IS NOT TRUE", nativeQuery = true)
    List<AuditorEntity> getAllActiveAuditors();
}
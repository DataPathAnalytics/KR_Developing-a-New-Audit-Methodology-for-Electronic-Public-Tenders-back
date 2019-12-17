package com.datapath.kg.risks.loader.dao.repository;

import com.datapath.kg.risks.loader.dao.entity.ReleaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReleaseRepository extends JpaRepository<ReleaseEntity, String> {

    ReleaseEntity findFirstByDateNotNullOrderByDateDesc();
}

package com.datapath.kg.risks.loader.dao.repository;

import com.datapath.kg.risks.loader.dao.entity.indicators.TenderIndicatorEntity;
import com.datapath.kg.risks.loader.dao.entity.indicators.TenderIndicatorId;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface TenderIndicatorRepository extends CrudRepository<TenderIndicatorEntity, TenderIndicatorId> {

    @Query(value = "DELETE FROM tender_indicator WHERE tender_id = ?1 AND indicator_id = ?2", nativeQuery = true)
    @Modifying
    @Transactional
    void delete(Integer tenderId, Integer indicatorId);
}

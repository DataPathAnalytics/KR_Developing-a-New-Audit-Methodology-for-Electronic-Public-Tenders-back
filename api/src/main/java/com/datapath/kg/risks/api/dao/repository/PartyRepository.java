package com.datapath.kg.risks.api.dao.repository;

import com.datapath.kg.risks.api.dao.entity.BuyerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PartyRepository extends JpaRepository<BuyerEntity, Integer> {

    @Query(value = "SELECT * FROM party WHERE identifier_id ILIKE CONCAT('%', :value , '%') " +
            "OR identifier_legal_name_ru ILIKE CONCAT('%', :value, '%') LIMIT 10", nativeQuery = true)
    List<BuyerEntity> findByIdentifierOrLegalName(@Param("value") String value);

}
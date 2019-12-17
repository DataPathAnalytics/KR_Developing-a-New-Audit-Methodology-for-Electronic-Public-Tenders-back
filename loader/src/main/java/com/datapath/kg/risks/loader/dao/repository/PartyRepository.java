package com.datapath.kg.risks.loader.dao.repository;

import com.datapath.kg.risks.loader.dao.entity.PartyEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PartyRepository extends CrudRepository<PartyEntity, Integer> {

    Optional<PartyEntity> findByOuterId(String outerId);

    @Query(value = "SELECT * FROM party WHERE identifier_id ILIKE CONCAT('%', :value , '%') OR identifier_legal_name_ru ILIKE CONCAT('%', :value, '%') LIMIT 10", nativeQuery = true)
    List<PartyEntity> findbyIdentifierOrLegalName(String value);

}

package com.datapath.kg.risks.loader.dao.repository;

import com.datapath.kg.risks.loader.dao.entity.AwardEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AwardRepository extends CrudRepository<AwardEntity, Integer> {

    List<AwardEntity> findByTenderId(Integer tenderId);
}

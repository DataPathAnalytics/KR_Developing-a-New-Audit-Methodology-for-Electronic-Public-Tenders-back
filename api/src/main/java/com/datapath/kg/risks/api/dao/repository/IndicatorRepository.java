package com.datapath.kg.risks.api.dao.repository;

import com.datapath.kg.risks.api.dao.entity.IndicatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IndicatorRepository extends JpaRepository<IndicatorEntity, Integer> {

    List<IndicatorEntity> findAllByOrderById();
}

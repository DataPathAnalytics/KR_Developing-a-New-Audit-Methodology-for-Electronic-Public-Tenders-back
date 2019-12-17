package com.datapath.kg.risks.api.dao.repository;

import com.datapath.kg.risks.api.dao.entity.TenderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenderRepository extends JpaRepository<TenderEntity, Integer> {
}

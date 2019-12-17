package com.datapath.kg.risks.loader.dao.repository;

import com.datapath.kg.risks.loader.dao.entity.ContactPointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactPointRepository extends JpaRepository<ContactPointEntity, Integer> {

    void deleteAllByTenderId(Integer tenderId);
}

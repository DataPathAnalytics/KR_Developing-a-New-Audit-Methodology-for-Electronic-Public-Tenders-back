package com.datapath.kg.risks.api.dao.repository;

import com.datapath.kg.risks.api.dao.entity.AuditorEntity;
import com.datapath.kg.risks.api.dao.entity.TemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemplateRepository extends JpaRepository<TemplateEntity, Integer> {

    List<TemplateEntity> findByBaseTrueOrAuditor(AuditorEntity auditor);

}

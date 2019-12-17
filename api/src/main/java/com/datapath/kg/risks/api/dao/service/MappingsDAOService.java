package com.datapath.kg.risks.api.dao.service;

import com.datapath.kg.risks.api.dao.entity.*;

import java.util.List;

public interface MappingsDAOService {

    List<RiskLevelEntity> getRiskLevels();

    List<IndicatorEntity> getIndicators();

    IndicatorEntity getIndicator(Integer id);

    List<String> getRegions();

    List<AnswerTypeEntity> getAnswerTypes();

    AnswerTypeEntity getAnswerType(Integer id);

    List<ComponentImpactEntity> getComponentImpacts();

    ComponentImpactEntity getComponentImpact(Integer id);

    List<ChecklistScoreEntity> getChecklistScores();

    ChecklistScoreEntity getChecklistScore(Integer id);

    List<ChecklistStatusEntity> getChecklistStatuses();

    ChecklistStatusEntity getChecklistStatus(Integer id);

    List<OkgzEntity> getOkgz();

    List<TemplateTypeEntity> getTemplateTypes();
}

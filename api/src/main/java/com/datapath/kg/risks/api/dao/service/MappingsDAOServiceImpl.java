package com.datapath.kg.risks.api.dao.service;

import com.datapath.kg.risks.api.dao.entity.*;
import com.datapath.kg.risks.api.dao.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MappingsDAOServiceImpl implements MappingsDAOService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AnswerTypeRepository answerTypeRepository;
    @Autowired
    private ComponentImpactRepository componentImpactRepository;
    @Autowired
    private ChecklistScoreRepository checklistScoreRepository;
    @Autowired
    private OkgzDAOService okgzDAOService;
    @Autowired
    private IndicatorRepository indicatorRepository;
    @Autowired
    private TemplateTypeRepository templateTypeRepository;
    @Autowired
    private ChecklistStatusRepository checklistStatusRepository;

    @Override
    public List<RiskLevelEntity> getRiskLevels() {
        return jdbcTemplate.query("SELECT id, description AS name, description_en AS name_en FROM buyer_risk_level_range ORDER BY id", new BeanPropertyRowMapper<>(RiskLevelEntity.class));
    }

    @Override
    public List<IndicatorEntity> getIndicators() {
        return indicatorRepository.findAllByOrderById();
    }

    @Override
    public IndicatorEntity getIndicator(Integer id) {
        return indicatorRepository.getOne(id);
    }

    @Override
    public List<AnswerTypeEntity> getAnswerTypes() {
        return answerTypeRepository.findAll();
    }

    @Override
    public AnswerTypeEntity getAnswerType(Integer id) {
        return answerTypeRepository.getOne(id);
    }

    @Override
    public List<ComponentImpactEntity> getComponentImpacts() {
        return componentImpactRepository.findAll();
    }

    @Override
    public ComponentImpactEntity getComponentImpact(Integer id) {
        return componentImpactRepository.getOne(id);
    }

    @Override
    public List<ChecklistScoreEntity> getChecklistScores() {
        return checklistScoreRepository.findAll();
    }

    @Override
    public ChecklistScoreEntity getChecklistScore(Integer id) {
        return checklistScoreRepository.getOne(id);
    }

    @Override
    public List<String> getRegions() {
        return jdbcTemplate.queryForList("SELECT DISTINCT region FROM buyer_prioritization WHERE region IS NOT NULL", String.class);
    }

    @Override
    public List<ChecklistStatusEntity> getChecklistStatuses() {
        return checklistStatusRepository.findAll();
    }

    @Override
    public ChecklistStatusEntity getChecklistStatus(Integer id) {
        return checklistStatusRepository.getOne(id);
    }

    public List<OkgzEntity> getOkgz() {
        return okgzDAOService.getAll();
    }

    @Override
    public List<TemplateTypeEntity> getTemplateTypes() {
        return templateTypeRepository.findAll();
    }
}

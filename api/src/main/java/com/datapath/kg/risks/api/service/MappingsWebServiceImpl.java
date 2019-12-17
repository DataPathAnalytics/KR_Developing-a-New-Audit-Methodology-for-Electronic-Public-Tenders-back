package com.datapath.kg.risks.api.service;

import com.datapath.kg.risks.api.DTOEntityMapper;
import com.datapath.kg.risks.api.dao.service.MappingsDAOService;
import com.datapath.kg.risks.api.dto.AnswerTypeDTO;
import com.datapath.kg.risks.api.dto.ChecklistScoreDTO;
import com.datapath.kg.risks.api.dto.ComponentImpactDTO;
import com.datapath.kg.risks.api.dto.MappingsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.datapath.kg.risks.api.Constants.*;
import static java.util.stream.Collectors.toList;

@Service
public class MappingsWebServiceImpl implements MappingsWebService {

    @Autowired
    private MappingsDAOService dao;
    @Autowired
    private DTOEntityMapper mapper;

    @Transactional
    @Override
    public MappingsDTO getMappings() {
        MappingsDTO mappings = new MappingsDTO();
        mappings.setProcurementMethodDetails(PROCUREMENT_METHOD_DETAILS_MAPPING);

        mappings.setRiskLevels(mapper.mapRiskLevels(dao.getRiskLevels()));
        mappings.setIndicators(mapper.mapIndicators(dao.getIndicators()));
        mappings.setRegions(REGIONS);
        List<AnswerTypeDTO> answerTypes = mapper.mapAnswerTypes(dao.getAnswerTypes());

        mappings.setQuestionAnswers(answerTypes);
        mappings.setIndicatorAnswers(answerTypes.stream().filter(answer -> answer.getId() != 3).collect(toList()));

        List<ComponentImpactDTO> componentImpacts = mapper.mapComponentImpacts(dao.getComponentImpacts());
        mappings.setComponentImpacts(componentImpacts);

        List<ChecklistScoreDTO> checklistScores = mapper.mapChecklists(dao.getChecklistScores());
        mappings.setChecklistScores(checklistScores);

        mappings.setChecklistStatuses(mapper.mapChecklistStatuses(dao.getChecklistStatuses()));

        mappings.setOkgz(mapper.mapOkgz(dao.getOkgz()));

        mappings.setTemplateTypes(mapper.mapTemplateTypes(dao.getTemplateTypes()));

        mappings.setDashboardTenderChecklistFilter(TENDER_DASHBOARD_CHECKLIST_FILTER);

        mappings.setIndicatorStatuses(INDICATOR_STATUS);

        return mappings;
    }

}
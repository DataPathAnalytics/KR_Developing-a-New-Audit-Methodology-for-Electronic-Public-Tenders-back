package com.datapath.kg.risks.api;

import com.datapath.kg.risks.api.dao.entity.*;
import com.datapath.kg.risks.api.dao.service.AuditorDAOService;
import com.datapath.kg.risks.api.dao.service.BuyerDAOService;
import com.datapath.kg.risks.api.dao.service.MappingsDAOService;
import com.datapath.kg.risks.api.dao.service.TenderDAOService;
import com.datapath.kg.risks.api.dto.ChecklistDTO;
import com.datapath.kg.risks.api.dto.TenderIndicatorDTO;
import com.datapath.kg.risks.api.request.SaveChecklistRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.springframework.util.CollectionUtils.isEmpty;

@Component
public class EntityConverterImpl implements EntityConverter {

    @Autowired
    private TenderDAOService tenderDAO;

    @Autowired
    private BuyerDAOService buyerDAO;

    @Autowired
    private MappingsDAOService mappingsDAO;

    @Autowired
    private AuditorDAOService auditorDAO;

    @Autowired
    private DTOEntityMapper mapper;

    @Override
    public ChecklistEntity convert(SaveChecklistRequest request) {
        AuditorEntity auditor = auditorDAO.getCurrent();

        ChecklistEntity checklist = new ChecklistEntity();
        checklist.setId(request.getId());
        checklist.setAuditor(auditor);
        checklist.setName(request.getName());
        checklist.setStartDate(request.getStartDate());
        checklist.setEndDate(request.getEndDate());
        checklist.setAuditName(request.getAuditName());
        checklist.setSummary(request.getSummary());
        checklist.setContractNumber(request.getContractNumber());
        checklist.setContractAmount(request.getContractAmount());
        checklist.setContractDescription(request.getContractDescription());
        checklist.setSupplier(request.getSupplier());
        checklist.setTendersComment(request.getTendersComment());
        if (request.getTendersImpactId() != null) {
            ComponentImpactEntity tendersImpact = mappingsDAO.getComponentImpact(request.getTendersImpactId());
            checklist.setTendersImpact(tendersImpact);
        }

        if (request.getManualTendersScoreId() != null) {
            ChecklistScoreEntity tendersScore = mappingsDAO.getChecklistScore(request.getManualTendersScoreId());
            checklist.setManualTendersScore(tendersScore);
        }

        checklist.setTemplateName(request.getTemplateName());
        checklist.setTemplateTypeId(request.getTemplateTypeId());

        if (request.getBuyerId() != null) {
            BuyerEntity buyer = buyerDAO.findById(request.getBuyerId());
            checklist.setBuyer(buyer);
        }

        if (request.getTenderId() != null) {
            TenderEntity tender = tenderDAO.getTender(request.getTenderId());
            checklist.setTender(tender);
        }

        if (request.getStatusId() != null) {
            ChecklistStatusEntity status = mappingsDAO.getChecklistStatus(request.getStatusId());
            checklist.setStatus(status);
        }

        request.getAnswers().forEach(dto -> {
            AnswerEntity answer = new AnswerEntity();
            answer.setComment(dto.getComment());
            answer.setNpa(dto.getNpa());

            answer.setAnswerType(getAnswerType(dto.getAnswerTypeId()));
            answer.setComponentImpact(getImpact(dto.getComponentImpactId()));
            answer.setCategoryName(dto.getCategoryName());
            answer.setQuestionDescription(dto.getQuestionDescription());
            answer.setBaseQuestion(dto.isBaseQuestion());
            answer.setCategoryNumber(dto.getCategoryNumber());
            answer.setQuestionNumber(dto.getQuestionNumber());

            checklist.getAnswers().add(answer);
        });

        if (!isEmpty(request.getIndicators())) {
            request.getIndicators().forEach(indicator -> {
                ChecklistIndicatorEntity checklistIndicator = new ChecklistIndicatorEntity();
                checklistIndicator.setChecklist(checklist);
                checklistIndicator.setAnswerType(getAnswerType(indicator.getAnswerTypeId()));
                checklistIndicator.setComponentImpact(getImpact(indicator.getComponentImpactId()));

                checklistIndicator.setId(indicator.getId());
                checklistIndicator.setIndicator(mappingsDAO.getIndicator(indicator.getIndicatorId()));
                checklistIndicator.setComment(indicator.getComment());

                checklist.getIndicators().add(checklistIndicator);
            });
        }

        if (request.getManualScoreId() != null) {
            ChecklistScoreEntity manualScore = mappingsDAO.getChecklistScore(request.getManualScoreId());
            checklist.setManualScore(manualScore);
        }

        return checklist;
    }

    private AnswerTypeEntity getAnswerType(Integer id) {
        return id == null ? null : mappingsDAO.getAnswerType(id);
    }

    private ComponentImpactEntity getImpact(Integer id) {
        return id == null ? null : mappingsDAO.getComponentImpact(id);
    }

    @Override
    public TenderIndicatorDTO convert(TenderIndicatorEntity entity) {
        TenderIndicatorDTO dto = new TenderIndicatorDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setValue(entity.getIndicatorValue());
        return dto;
    }

    @Override
    public ChecklistDTO convert(ChecklistEntity entity) {
        return mapper.map(entity);
    }

}

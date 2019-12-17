package com.datapath.kg.risks.api.service;

import com.datapath.kg.risks.api.DTOEntityMapper;
import com.datapath.kg.risks.api.EntityConverter;
import com.datapath.kg.risks.api.comparators.AnswerComparator;
import com.datapath.kg.risks.api.dao.entity.AuditorEntity;
import com.datapath.kg.risks.api.dao.entity.ChecklistEntity;
import com.datapath.kg.risks.api.dao.entity.ChecklistScoreEntity;
import com.datapath.kg.risks.api.dao.entity.TenderPrioritizationEntity;
import com.datapath.kg.risks.api.dao.service.AuditorDAOService;
import com.datapath.kg.risks.api.dao.service.ChecklistDAOService;
import com.datapath.kg.risks.api.dao.service.MappingsDAOService;
import com.datapath.kg.risks.api.dao.service.PrioritizationDAOService;
import com.datapath.kg.risks.api.dto.ChecklistDTO;
import com.datapath.kg.risks.api.dto.ChecklistScoreDTO;
import com.datapath.kg.risks.api.exception.ChecklistDeleteException;
import com.datapath.kg.risks.api.exception.ChecklistSavingException;
import com.datapath.kg.risks.api.export.ChecklistPdfGenerator;
import com.datapath.kg.risks.api.request.ChecklistRequest;
import com.datapath.kg.risks.api.request.SaveChecklistRequest;
import com.datapath.kg.risks.api.request.TendersScoreRequest;
import com.datapath.kg.risks.api.response.ChecklistsResponse;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static com.datapath.kg.risks.api.Constants.COMPLETED_CHECKLIST_STATUS_ID;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class ChecklistWebServiceImpl implements ChecklistWebService {

    private static final int WITHOUT_IMPACT_ID = 1;
    private static final int CRITICAL_IMPACT_ID = 7;
    @Autowired
    private ChecklistDAOService checklistDAO;
    @Autowired
    private MappingsDAOService mappingsDAO;
    @Autowired
    private DTOEntityMapper mapper;
    @Autowired
    private EntityConverter converter;
    @Autowired
    private AuditorDAOService auditorDAOService;
    @Autowired
    private ChecklistPdfGenerator pdfGenerator;
    @Autowired
    private AnswerComparator answerComparator;
    @Autowired
    private PrioritizationDAOService prioritizationDAO;

    @Override
    public ChecklistsResponse getChecklists(ChecklistRequest request) {
        AuditorEntity auditor = auditorDAOService.getCurrent();
        ChecklistsResponse response = new ChecklistsResponse();
        List<ChecklistDTO> checklists = checklistDAO.getChecklists(request, auditor).stream().map(mapper::map).collect(toList());
        response.setChecklists(checklists);
        return response;
    }

    @Override
    public ChecklistDTO saveChecklist(SaveChecklistRequest request) {
        AuditorEntity auditor = auditorDAOService.getCurrent();
        ChecklistEntity previousChecklist = checklistDAO.getChecklist(request.getId());

        if (nonNull(previousChecklist) && !previousChecklist.getAuditor().equals(auditor)) {
            throw new ChecklistSavingException("You can't save changes for this checklist");
        }

        ChecklistEntity checklist = converter.convert(request);

        ChecklistScoreEntity score = getAutoChecklistScore(checklist);
        checklist.setAutoScore(score);

        if (checklist.getTemplateTypeId().equals(1)) {
            ChecklistScoreEntity tendersScore = getAutoTendersScore(new TendersScoreRequest(
                    checklist.getBuyer().getId(),
                    checklist.getStartDate(),
                    checklist.getEndDate())
            );
            checklist.setAutoTendersScore(tendersScore);
        }
        return mapper.map(checklistDAO.save(checklist));
    }

    @Override
    public ChecklistScoreDTO calcAutoScore(SaveChecklistRequest request) {
        ChecklistEntity checklist = converter.convert(request);
        ChecklistScoreEntity score = getAutoChecklistScore(checklist);
        return mapper.map(score);
    }

    private ChecklistScoreEntity getAutoChecklistScore(ChecklistEntity checklist) {
        List<ChecklistScoreEntity> scores = mappingsDAO.getChecklistScores();

        IntStream questionImpacts = checklist.getAnswers().stream()
                .filter(answer -> answer.getComponentImpact() != null
                        && answer.getComponentImpact().getId() != WITHOUT_IMPACT_ID
                        && answer.getComponentImpact().getId() != CRITICAL_IMPACT_ID)
                .mapToInt(answer -> answer.getComponentImpact().getImpact());

        IntStream indicatorImpacts = IntStream.empty();
        if (!isEmpty(checklist.getIndicators())) {
            indicatorImpacts = checklist.getIndicators().stream()
                    .filter(indicator -> indicator.getComponentImpact() != null
                            && indicator.getComponentImpact().getId() != CRITICAL_IMPACT_ID)
                    .mapToInt(indicator -> indicator.getComponentImpact().getImpact());
        }

        IntStream allImpacts = IntStream.concat(questionImpacts, indicatorImpacts);

        if (checklist.getTendersImpact() != null && checklist.getTendersImpact().getId() != CRITICAL_IMPACT_ID) {
            allImpacts = IntStream.concat(allImpacts, IntStream.of(checklist.getTendersImpact().getImpact()));
        }

        double averageImpact = allImpacts.average().orElse(0);

        return scores.stream()
                .filter(score -> averageImpact >= score.getLeftBound() && averageImpact < score.getRightBound())
                .findFirst().orElse(null);
    }

    @Override
    public ResponseEntity export(Integer id) throws DocumentException, IOException {
        return getResponseWithPdfFile(pdfGenerator.pushDataToByteArray(id));
    }

    @Override
    public ChecklistDTO getChecklist(Integer id) {
        ChecklistEntity entity = checklistDAO.getChecklist(id);
        ChecklistDTO dto = converter.convert(entity);
        dto.getAnswers().sort(answerComparator);
        return dto;
    }

    @Override
    public void deleteChecklist(Integer id) {
        AuditorEntity auditor = auditorDAOService.getCurrent();
        boolean isAdmin = auditor.getPermissions().stream().anyMatch(perm -> perm.getName().equalsIgnoreCase("admin.base"));

        ChecklistEntity checklist = checklistDAO.getChecklist(id);
        if (isAdmin) {
            if (COMPLETED_CHECKLIST_STATUS_ID.equals(checklist.getStatus().getId())) {
                checklistDAO.deleteChecklist(id);
            } else {
                throw new ChecklistDeleteException("You can't delete this checklist");
            }
        } else {
            if (checklist.getAuditor().getId().equals(auditor.getId())) {
                checklistDAO.deleteChecklist(id);
            } else {
                throw new ChecklistDeleteException("You can't delete this checklist");
            }
        }
    }

    @Override
    public ChecklistScoreDTO calcAutoTendersScore(TendersScoreRequest request) {
        ChecklistScoreEntity score = getAutoTendersScore(request);
        return mapper.map(score);
    }

    private ChecklistScoreEntity getAutoTendersScore(TendersScoreRequest request) {
        List<ChecklistScoreEntity> scores = mappingsDAO.getChecklistScores();

        List<ChecklistEntity> tenderChecklists = checklistDAO.getTendersChecklistsByBuyerId(request.getBuyerId());

        if (isEmpty(tenderChecklists)) return null;

        Map<ChecklistEntity, TenderPrioritizationEntity> prioritizationChecklistData = new HashMap<>();

        for (ChecklistEntity tenderChecklist : tenderChecklists) {
            TenderPrioritizationEntity tender = prioritizationDAO.getByIdAndContractDate(tenderChecklist.getTender().getId(),
                    request.getStartPeriodDate().toString(),
                    request.getEndPeriodDate().toString());
            if (!isNull(tender)) prioritizationChecklistData.put(tenderChecklist, tender);
        }

        long sumFactWithRisk = 0;
        long sumFact = 0;

        for (Map.Entry<ChecklistEntity, TenderPrioritizationEntity> entry : prioritizationChecklistData.entrySet()) {
            ChecklistEntity c = entry.getKey();
            TenderPrioritizationEntity p = entry.getValue();
            sumFactWithRisk += c.getManualScore().getId() * p.getCompletedLotValue();
            sumFact += p.getCompletedLotValue();
        }

        if (sumFact == 0) return null;

        Double result = (double) sumFactWithRisk / sumFact;

        return scores.stream()
                .filter(score -> result > score.getBuyerLeftBound() && result <= score.getBuyerRightBound())
                .findFirst()
                .orElse(null);
    }

    private ResponseEntity getResponseWithPdfFile(byte[] resource) {
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/pdf")
                .body(resource);
    }
}
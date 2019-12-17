package com.datapath.kg.risks.api.dao.service;

import com.datapath.kg.risks.api.dao.entity.AuditorEntity;
import com.datapath.kg.risks.api.dao.entity.ChecklistEntity;
import com.datapath.kg.risks.api.request.ChecklistRequest;

import java.util.List;

public interface ChecklistDAOService {

    List<ChecklistEntity> getChecklists(ChecklistRequest request, AuditorEntity auditor);

    ChecklistEntity save(ChecklistEntity entity);

    List<ChecklistEntity> searchByAuditName(String value);

    ChecklistEntity getChecklist(Integer id);

    void deleteChecklist(Integer id);

    List<ChecklistEntity> searchByAuditNameForAdmin(String value);

    List<Integer> getActiveChecklistsByAuditor(Integer auditorId);

    Integer getCompletedChecklistsCount(Integer auditorId);

    List<ChecklistEntity> getTendersChecklistsByBuyerId(Integer buyerId);
}

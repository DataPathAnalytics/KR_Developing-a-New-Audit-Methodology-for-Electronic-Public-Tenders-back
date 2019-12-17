package com.datapath.kg.risks.api.dao.service;

import com.datapath.kg.risks.api.dao.entity.AuditorEntity;
import com.datapath.kg.risks.api.dao.entity.ChecklistEntity;
import com.datapath.kg.risks.api.dao.repository.ChecklistRepository;
import com.datapath.kg.risks.api.dao.specification.ChecklistSpecification;
import com.datapath.kg.risks.api.request.ChecklistRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChecklistDAOServiceImpl implements ChecklistDAOService {

    @Autowired
    private ChecklistRepository repository;

    @Override
    public List<ChecklistEntity> getChecklists(ChecklistRequest request, AuditorEntity auditor) {
        return repository.findAll(new ChecklistSpecification(request, auditor));
    }

    @Override
    public ChecklistEntity save(ChecklistEntity entity) {
        return repository.save(entity);
    }

    @Override
    public List<ChecklistEntity> searchByAuditName(String value) {
        return repository.searchByAuditName(value);
    }

    @Override
    public ChecklistEntity getChecklist(Integer id) {
        return repository.getById(id);
    }

    @Override
    public void deleteChecklist(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<ChecklistEntity> searchByAuditNameForAdmin(String value) {
        return repository.searchByAuditNameForAdmin(value);
    }

    @Override
    public List<Integer> getActiveChecklistsByAuditor(Integer auditorId) {
        return repository.getActiveChecklistsByAuditor(auditorId);
    }

    @Override
    public Integer getCompletedChecklistsCount(Integer auditorId) {
        return repository.getCompletedChecklistCount(auditorId);
    }

    @Override
    public List<ChecklistEntity> getTendersChecklistsByBuyerId(Integer buyerId) {
        return repository.getTendersChecklistsByBuyerId(buyerId);
    }
}

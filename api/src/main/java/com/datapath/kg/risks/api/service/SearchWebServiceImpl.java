package com.datapath.kg.risks.api.service;

import com.datapath.kg.risks.api.DTOEntityMapper;
import com.datapath.kg.risks.api.dao.entity.AuditorEntity;
import com.datapath.kg.risks.api.dao.service.AuditorDAOService;
import com.datapath.kg.risks.api.dao.service.BuyerDAOService;
import com.datapath.kg.risks.api.dao.service.ChecklistDAOService;
import com.datapath.kg.risks.api.response.AuditNameResponse;
import com.datapath.kg.risks.api.response.AuditorNameResponse;
import com.datapath.kg.risks.api.response.BuyersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchWebServiceImpl implements SearchWebService {

    @Autowired
    private BuyerDAOService buyerDAOService;
    @Autowired
    private ChecklistDAOService checklistDAOService;
    @Autowired
    private AuditorDAOService auditorDAOService;
    @Autowired
    private DTOEntityMapper mapper;

    @Override
    public BuyersResponse searchBuyer(String value) {
        BuyersResponse response = new BuyersResponse();
        response.setBuyers(mapper.mapBuyers(buyerDAOService.search(value)));
        return response;
    }

    @Override
    public AuditNameResponse searchAuditName(String value) {
        AuditNameResponse response = new AuditNameResponse();
        AuditorEntity auditor = auditorDAOService.getCurrent();
        boolean isAdmin = auditor.getPermissions().stream().anyMatch(perm -> perm.getName().equalsIgnoreCase("admin.base"));
        if (isAdmin) {
            response.setAuditNameDetails(mapper.mapChecklistAuditName(checklistDAOService.searchByAuditNameForAdmin(value)));
        } else {
            response.setAuditNameDetails(mapper.mapChecklistAuditName(checklistDAOService.searchByAuditName(value)));
        }
        return response;
    }

    @Override
    public AuditorNameResponse searchAuditorName(String value) {
        AuditorNameResponse response = new AuditorNameResponse();
        AuditorEntity auditor = auditorDAOService.getCurrent();
        boolean isAdmin = auditor.getPermissions().stream().anyMatch(perm -> perm.getName().equalsIgnoreCase("admin.base"));
        if (isAdmin) {
            response.setAuditorNameDetails(mapper.mapChecklistAuditorName(auditorDAOService.searchByAuditorNameForAdmin(value)));
        } else {
            response.setAuditorNameDetails(mapper.mapChecklistAuditorName(auditorDAOService.searchByAuditorName(value)));
        }
        return response;
    }
}

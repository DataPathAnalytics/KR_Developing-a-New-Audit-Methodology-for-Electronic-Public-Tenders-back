package com.datapath.kg.risks.api.dao.service;

import com.datapath.kg.risks.api.dao.entity.AuditorEntity;
import com.datapath.kg.risks.api.dao.entity.BuyerPrioritizationEntity;
import com.datapath.kg.risks.api.dao.entity.TenderPrioritizationEntity;
import com.datapath.kg.risks.api.request.BuyerPrioritizationRequest;
import com.datapath.kg.risks.api.request.TenderPrioritizationRequest;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public interface PrioritizationDAOService {

    List<TenderPrioritizationEntity> getTenders(TenderPrioritizationRequest request, AuditorEntity auditor);

    List<BuyerPrioritizationEntity> getBuyers(BuyerPrioritizationRequest request, AuditorEntity auditor);

    void exportBuyers(BuyerPrioritizationRequest request, AuditorEntity auditor, Sheet sheet);

    void exportTenders(TenderPrioritizationRequest request, AuditorEntity auditor, Sheet sheet);

    TenderPrioritizationEntity getByIdAndContractDate(Integer id, String startDate, String endDate);

    TenderPrioritizationEntity getTenderPrioritizationById(Integer id);
}

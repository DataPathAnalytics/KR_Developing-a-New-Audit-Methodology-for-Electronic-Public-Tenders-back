package com.datapath.kg.risks.api.service;

import com.datapath.kg.risks.api.DTOEntityMapper;
import com.datapath.kg.risks.api.dao.entity.*;
import com.datapath.kg.risks.api.dao.service.AuditorDAOService;
import com.datapath.kg.risks.api.dao.service.PrioritizationDAOService;
import com.datapath.kg.risks.api.dao.service.TenderDAOService;
import com.datapath.kg.risks.api.dto.BuyerPrioritizationDTO;
import com.datapath.kg.risks.api.dto.ColumnDetailsDTO;
import com.datapath.kg.risks.api.dto.TenderPrioritizationDTO;
import com.datapath.kg.risks.api.exception.PrioritizationExportException;
import com.datapath.kg.risks.api.request.BuyerPrioritizationRequest;
import com.datapath.kg.risks.api.request.TenderPrioritizationRequest;
import com.datapath.kg.risks.api.response.PrioritizationBuyersResponse;
import com.datapath.kg.risks.api.response.PrioritizationTendersResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
public class PrioritizationWebServiceImpl implements PrioritizationWebService {

    @Autowired
    private PrioritizationDAOService dao;
    @Autowired
    private TenderDAOService tenderDAO;
    @Autowired
    private DTOEntityMapper mapper;
    @Autowired
    private AuditorDAOService auditorDAOService;

    @Override
    public PrioritizationTendersResponse getTenders(TenderPrioritizationRequest request) {
        AuditorEntity auditor = auditorDAOService.getCurrent();
        PrioritizationTendersResponse response = new PrioritizationTendersResponse();
        List<TenderPrioritizationEntity> tenderEntities = dao.getTenders(request, auditor);
        List<TenderPrioritizationDTO> tenders = tenderEntities.stream().map(entity -> mapper.map(entity)).collect(toList());

        if (request.getTenderId() != null && !isEmpty(tenders)) {
            TenderPrioritizationDTO tenderDTO = tenders.get(0);
            TenderEntity tenderEntity = tenderDAO.getTender(request.getTenderId());

            LocalDate dateDisclosed = getDateDisclosed(tenderEntity);

            tenderDTO.setPeriodStartDate(tenderEntity.getPeriodStartDate());
            tenderDTO.setPeriodEndDate(tenderEntity.getPeriodEndDate());
            tenderDTO.setGuaranteeAmount(tenderEntity.getGuaranteeAmount());
            tenderDTO.setDateDisclosed(dateDisclosed);
        }

        response.setTenders(tenders);
        return response;
    }

    private LocalDate getDateDisclosed(TenderEntity tender) {
        if (isEmpty(tender.getBids())) return null;

        return tender
                .getBids()
                .stream()
                .filter(b -> nonNull(b.getDateDisclosed()))
                .findFirst()
                .map(BidEntity::getDateDisclosed).orElse(null);
    }

    @Override
    public PrioritizationBuyersResponse getBuyers(BuyerPrioritizationRequest request) {
        AuditorEntity auditor = auditorDAOService.getCurrent();
        PrioritizationBuyersResponse response = new PrioritizationBuyersResponse();
        List<BuyerPrioritizationEntity> buyerEntities = dao.getBuyers(request, auditor);
        List<BuyerPrioritizationDTO> buyers = buyerEntities.stream().map(entity -> mapper.map(entity)).collect(toList());
        response.setBuyers(buyers);
        return response;
    }

    @Override
    public ResponseEntity getTendersFile(TenderPrioritizationRequest request) throws IOException {
        AuditorEntity auditor = auditorDAOService.getCurrent();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");
        addHeadersToSheet(sheet, request.getColumns());
        dao.exportTenders(request, auditor, sheet);
        return getResponseWithFile(workbook);
    }

    @Override
    public ResponseEntity getBuyersFile(BuyerPrioritizationRequest request) throws IOException {
        AuditorEntity auditor = auditorDAOService.getCurrent();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");
        addHeadersToSheet(sheet, request.getColumns());
        dao.exportBuyers(request, auditor, sheet);
        return getResponseWithFile(workbook);
    }

    private void addHeadersToSheet(Sheet sheet, List<ColumnDetailsDTO> columnDetails) {
        Row headers = sheet.createRow(0);

        if (isEmpty(columnDetails)) throw new PrioritizationExportException("Not present selected columns.");

        for (int i = 0; i < columnDetails.size(); i++) {
            Cell header = headers.createCell(i);
            header.setCellValue(columnDetails.get(i).getTranslate());
        }
    }

    private ResponseEntity getResponseWithFile(Workbook workbook) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(outputStream.toByteArray());
    }
}
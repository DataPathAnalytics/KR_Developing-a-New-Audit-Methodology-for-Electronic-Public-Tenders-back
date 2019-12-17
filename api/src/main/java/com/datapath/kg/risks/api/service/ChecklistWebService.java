package com.datapath.kg.risks.api.service;

import com.datapath.kg.risks.api.dto.ChecklistDTO;
import com.datapath.kg.risks.api.dto.ChecklistScoreDTO;
import com.datapath.kg.risks.api.request.ChecklistRequest;
import com.datapath.kg.risks.api.request.SaveChecklistRequest;
import com.datapath.kg.risks.api.request.TendersScoreRequest;
import com.datapath.kg.risks.api.response.ChecklistsResponse;
import com.itextpdf.text.DocumentException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface ChecklistWebService {

    ChecklistsResponse getChecklists(ChecklistRequest request);

    ChecklistDTO saveChecklist(SaveChecklistRequest request);

    ChecklistScoreDTO calcAutoScore(SaveChecklistRequest request);

    ResponseEntity export(Integer id) throws DocumentException, IOException;

    ChecklistDTO getChecklist(Integer id);

    void deleteChecklist(Integer id);

    ChecklistScoreDTO calcAutoTendersScore(TendersScoreRequest buyerId);
}
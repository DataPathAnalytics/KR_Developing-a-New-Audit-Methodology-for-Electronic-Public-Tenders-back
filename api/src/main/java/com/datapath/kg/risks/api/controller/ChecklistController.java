package com.datapath.kg.risks.api.controller;

import com.datapath.kg.risks.api.dto.ChecklistDTO;
import com.datapath.kg.risks.api.dto.ChecklistScoreDTO;
import com.datapath.kg.risks.api.request.ChecklistRequest;
import com.datapath.kg.risks.api.request.SaveChecklistRequest;
import com.datapath.kg.risks.api.request.TendersScoreRequest;
import com.datapath.kg.risks.api.response.ChecklistsResponse;
import com.datapath.kg.risks.api.service.ChecklistWebService;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("checklists")
public class ChecklistController {

    @Autowired
    private ChecklistWebService service;

    @PostMapping
    public ChecklistsResponse getChecklists(@RequestBody ChecklistRequest request) {
        return service.getChecklists(request);
    }

    @PostMapping("save")
    @PreAuthorize("hasAnyAuthority('auditor.base')")
    public ChecklistDTO saveChecklist(@Valid @RequestBody SaveChecklistRequest request) {
        return service.saveChecklist(request);
    }

    @PostMapping("auto-score")
    public ChecklistScoreDTO calcAutoScore(@RequestBody SaveChecklistRequest request) {
        return service.calcAutoScore(request);
    }

    @GetMapping("{id}/export")
    public ResponseEntity export(@PathVariable Integer id) throws IOException, DocumentException {
        return service.export(id);
    }

    @GetMapping("{id}")
    public ChecklistDTO getChecklist(@PathVariable Integer id) {
        return service.getChecklist(id);
    }

    @DeleteMapping("{id}")
    public void deleteChecklist(@PathVariable Integer id) {
        service.deleteChecklist(id);
    }

    @PostMapping("auto-tenders-score")
    public ChecklistScoreDTO calcAutoTendersScore(@RequestBody TendersScoreRequest request) {
        return service.calcAutoTendersScore(request);
    }
}

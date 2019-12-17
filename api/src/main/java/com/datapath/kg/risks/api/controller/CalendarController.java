package com.datapath.kg.risks.api.controller;

import com.datapath.kg.risks.api.dao.entity.WorkDateEntity;
import com.datapath.kg.risks.api.dao.service.WorkDateDAOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("calendar")
@CrossOrigin
public class CalendarController {

    @Autowired
    private WorkDateDAOService service;

    @GetMapping
    public List<WorkDateEntity> getByYear(@RequestParam Integer year) {
        return service.getByYear(year);
    }

    @PostMapping
    public void save(@RequestBody List<WorkDateEntity> dates) {
        service.save(dates);
    }
}

package com.datapath.kg.risks.api.dao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@Entity(name = "work_calendar")
public class WorkDateEntity {

    @Id
    private LocalDate date;
    private Boolean isWorking;
}

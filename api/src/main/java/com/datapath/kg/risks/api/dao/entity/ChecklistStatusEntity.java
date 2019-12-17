package com.datapath.kg.risks.api.dao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "checklist_status")
public class ChecklistStatusEntity {

    @Id
    private Integer id;
    private String name;
    private String nameEn;
}

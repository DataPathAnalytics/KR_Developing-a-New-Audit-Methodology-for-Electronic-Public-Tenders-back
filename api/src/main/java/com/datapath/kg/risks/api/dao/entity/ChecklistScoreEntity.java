package com.datapath.kg.risks.api.dao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "checklist_score")
public class ChecklistScoreEntity {

    @Id
    private Integer id;
    private String name;
    private String nameEn;
    private Integer leftBound;
    private Integer rightBound;
    private Integer buyerLeftBound;
    private Integer buyerRightBound;

}
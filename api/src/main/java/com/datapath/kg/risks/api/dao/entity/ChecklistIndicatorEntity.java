package com.datapath.kg.risks.api.dao.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "checklist_indicator")
public class ChecklistIndicatorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private IndicatorEntity indicator;

    @ManyToOne
    @JoinColumn(name = "answer_type_id")
    private AnswerTypeEntity answerType;

    @ManyToOne
    @JoinColumn(name = "component_impact_id")
    private ComponentImpactEntity componentImpact;

    @ManyToOne
    @JoinColumn(name = "checklist_id")
    private ChecklistEntity checklist;

    private String comment;
}
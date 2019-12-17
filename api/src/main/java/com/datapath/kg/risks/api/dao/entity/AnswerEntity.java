package com.datapath.kg.risks.api.dao.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "question_answer")
public class AnswerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String comment;
    private String npa;

    @ManyToOne
    @JoinColumn(name = "answer_type_id")
    private AnswerTypeEntity answerType;

    @ManyToOne
    @JoinColumn(name = "component_impact_id")
    private ComponentImpactEntity componentImpact;

    private String categoryName;
    private String categoryNumber;
    private String questionDescription;
    private Boolean baseQuestion;
    private String questionNumber;

}
package com.datapath.kg.risks.api.dao.entity;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "checklist_question")
@Data
public class ChecklistQuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private QuestionCategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "checklist_id")
    private ChecklistEntity checklist;

}
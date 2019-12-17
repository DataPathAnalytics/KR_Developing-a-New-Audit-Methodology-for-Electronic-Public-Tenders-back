package com.datapath.kg.risks.api.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity(name = "question_category")
@Data
public class QuestionCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;
    private String number;

    @Column(name = "template_id")
    private Integer templateId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private List<QuestionEntity> questions;

}
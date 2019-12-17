package com.datapath.kg.risks.api.dao.entity;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "question")
@Data
public class QuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String description;

    private boolean base;

    @Column(name = "category_id")
    private Integer categoryId;

    private String number;
}
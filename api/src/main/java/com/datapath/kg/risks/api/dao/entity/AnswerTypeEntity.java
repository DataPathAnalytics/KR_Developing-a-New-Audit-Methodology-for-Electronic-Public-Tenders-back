package com.datapath.kg.risks.api.dao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "answer_type")
public class AnswerTypeEntity {

    @Id
    private Integer id;
    private String name;
    private String nameEn;

}

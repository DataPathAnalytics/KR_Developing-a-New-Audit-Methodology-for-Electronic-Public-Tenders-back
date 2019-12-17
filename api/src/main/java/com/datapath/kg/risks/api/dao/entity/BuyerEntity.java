package com.datapath.kg.risks.api.dao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "party")
public class BuyerEntity {

    @Id
    private Integer id;
    private String identifierId;
    private String identifierLegalNameRu;

}
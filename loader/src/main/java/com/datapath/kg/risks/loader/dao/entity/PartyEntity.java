package com.datapath.kg.risks.loader.dao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity(name = "party")
public class PartyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String outerId;
    private String identifierId;
    private String identifierScheme;
    private String identifierLegalName;
    private String identifierLegalNameRu;
    private String identifierLegalNameKg;
    private String district;
    private String locality;
    private String streetAddress;
    private String region;
}

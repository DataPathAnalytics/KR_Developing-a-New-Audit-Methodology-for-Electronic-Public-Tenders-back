package com.datapath.kg.risks.api.dao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "okgz")
public class OkgzEntity {

    @Id
    private String code;
    private String originalCode;
    private String name;
    private String nameEn;
}

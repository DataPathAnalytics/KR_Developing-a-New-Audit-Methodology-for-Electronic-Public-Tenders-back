package com.datapath.kg.risks.api.dao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "component_impact")
public class ComponentImpactEntity {

    @Id
    private Integer id;
    private String name;
    private String nameEn;
    private Integer impact;

}
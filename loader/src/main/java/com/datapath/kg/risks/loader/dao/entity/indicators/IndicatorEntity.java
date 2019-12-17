package com.datapath.kg.risks.loader.dao.entity.indicators;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "indicator")
public class IndicatorEntity {

    @Id
    private Integer id;
    private String name;
    private Double impact;

}

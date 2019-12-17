package com.datapath.kg.risks.loader.dao.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
class ContactPointId implements Serializable {

    private Integer id;
    private Integer tenderId;
}

package com.datapath.kg.risks.api.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@Entity(name = "bid")
@EqualsAndHashCode(of = "id")
public class BidEntity {

    @Id
    private Integer id;
    private LocalDate dateDisclosed;
}

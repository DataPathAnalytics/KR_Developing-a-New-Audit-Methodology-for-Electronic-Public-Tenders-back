package com.datapath.kg.risks.api.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@Entity(name="contract")
@EqualsAndHashCode(of = "id")
public class ContractEntity {
    @Id
    private Integer id;
    private LocalDate dateSigned;
}

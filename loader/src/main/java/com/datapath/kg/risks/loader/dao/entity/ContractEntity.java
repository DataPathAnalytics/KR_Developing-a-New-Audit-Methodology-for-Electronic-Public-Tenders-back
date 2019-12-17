package com.datapath.kg.risks.loader.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity(name="contract")
@EqualsAndHashCode(of = "id")
public class ContractEntity {

    @Id
    private Integer id;
    private LocalDateTime dateSigned;
    private String status;
    private Double valueAmount;

    @ManyToOne(cascade = CascadeType.ALL, fetch = LAZY)
    @JoinColumn(name = "tender_id")
    private TenderEntity tender;
//
//    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(name = "contract_award",
//            joinColumns = {@JoinColumn(name = "contract_id")},
//            inverseJoinColumns = {@JoinColumn(name = "award_id")})
//    private Set<AwardEntity> awards = new HashSet<>();
}

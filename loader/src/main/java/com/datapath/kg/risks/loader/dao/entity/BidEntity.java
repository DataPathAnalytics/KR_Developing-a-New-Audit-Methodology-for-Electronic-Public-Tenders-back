package com.datapath.kg.risks.loader.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity(name = "bid")
@EqualsAndHashCode(of = "id")
public class BidEntity {

    @Id
    private Integer id;
    private String status;
    private String bidderId;
    private LocalDateTime dateDisclosed;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "bid_id", referencedColumnName = "id")
    private Set<BidLotEntity> bidLots = new HashSet<>();

    @ManyToOne(cascade = CascadeType.ALL, fetch = LAZY)
    @JoinColumn(name = "tender_id")
    private TenderEntity tender;

    @OneToMany(mappedBy = "bid", cascade = CascadeType.ALL)
    private Set<PriceProposalEntity> priceProposal = new HashSet<>();
}

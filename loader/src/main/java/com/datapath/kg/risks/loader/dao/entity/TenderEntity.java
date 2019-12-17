package com.datapath.kg.risks.loader.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Data
@Entity(name = "tender")
@EqualsAndHashCode(of = "id")
public class TenderEntity {
    @Id
    private Integer id;
    private Integer buyerId;
    private String status;
    private String currentStage;
    private LocalDateTime date;
    private LocalDateTime datePublished;
    private LocalDateTime periodStartDate;
    private LocalDateTime periodEndDate;
    private String procurementMethodRationale;
    private String procurementMethodDetails;
    private Double amount;
    private String currency;
    private Double guaranteeAmount;
    private String number;
    private boolean badQuality;
    private boolean hasDocuments;

    @OneToOne(cascade = ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ocid")
    private ReleaseEntity release;

    @OneToMany(mappedBy = "tender", cascade = ALL)
    private Set<LotEntity> lots = new HashSet<>();

    @OneToMany(mappedBy = "tender", cascade = ALL)
    private Set<ItemEntity> items = new HashSet<>();

    @OneToMany(mappedBy = "tender", cascade = ALL)
    private Set<AwardEntity> awards = new HashSet<>();

    @OneToMany(mappedBy = "tender", cascade = ALL)
    private Set<QualificationRequirementEntity> qualificationRequirements = new HashSet<>();

    @OneToMany(mappedBy = "tender", cascade = ALL)
    private Set<EnquiryEntity> enquiries = new HashSet<>();

    @OneToMany(mappedBy = "tender", cascade = ALL)
    private Set<RelatedProcessEntity> relatedProcesses = new HashSet<>();

    @OneToMany(mappedBy = "tender", cascade = ALL)
    private Set<BidEntity> bids = new HashSet<>();

    @OneToMany(mappedBy = "tender", cascade = ALL)
    private Set<ComplaintEntity> complaints = new HashSet<>();

    @OneToMany(mappedBy = "tender", cascade = ALL)
    private Set<ContractEntity> contracts = new HashSet<>();

    @ManyToMany(cascade = ALL)
    @JoinTable(name = "tender_supplier",
            joinColumns = @JoinColumn(name = "tender_id"),
            inverseJoinColumns = @JoinColumn(name = "party_id")
    )
    private Set<PartyEntity> suppliers = new HashSet<>();
}
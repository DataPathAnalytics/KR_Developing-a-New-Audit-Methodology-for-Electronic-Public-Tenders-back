package com.datapath.kg.risks.api.dao.entity;

import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "checklist")
public class ChecklistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    private String templateName;
    private Integer templateTypeId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "checklist_id")
    @OrderBy("category_number, question_number")
    private List<AnswerEntity> answers = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "checklist_id")
    private List<ChecklistIndicatorEntity> indicators = new ArrayList<>();

    @ManyToOne
    private ChecklistScoreEntity autoScore;
    @ManyToOne
    private ChecklistScoreEntity manualScore;
    @ManyToOne
    private ChecklistScoreEntity manualTendersScore;
    @ManyToOne
    private ChecklistScoreEntity autoTendersScore;

    private String tendersComment;

    @ManyToOne
    @JoinColumn(name = "tenders_impact_id")
    private ComponentImpactEntity tendersImpact;

    @ManyToOne(optional = false)
    @JoinColumn(name = "status_id")
    private ChecklistStatusEntity status;

    @ManyToOne
    private BuyerEntity buyer;

    @ManyToOne
    private TenderEntity tender;

    @ManyToOne
    private AuditorEntity auditor;

    @UpdateTimestamp
    private LocalDate modifiedDate;

    private String summary;
    private String auditName;
    private String contractNumber;
    private Double contractAmount;
    private String contractDescription;
    private String supplier;
}
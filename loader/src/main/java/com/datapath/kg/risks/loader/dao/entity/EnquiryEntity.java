package com.datapath.kg.risks.loader.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity(name = "enquiry")
public class EnquiryEntity {

    @Id
    private Integer id;
    private LocalDateTime date;
    private LocalDateTime dateAnswered;

    @ManyToOne(cascade = CascadeType.ALL, fetch = LAZY)
    @JoinColumn(name = "tender_id")
    private TenderEntity tender;
}

package com.datapath.kg.risks.loader.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity(name = "release")
public class ReleaseEntity {

    @Id
    private String ocid;
    private LocalDateTime date;

    @OneToOne(mappedBy = "release", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TenderEntity tender;

}
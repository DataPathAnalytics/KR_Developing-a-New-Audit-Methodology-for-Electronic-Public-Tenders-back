package com.datapath.kg.risks.api.dao.entity;

import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity(name = "template")
@Data
@EntityListeners(AuditingEntityListener.class)
public class TemplateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;
    private boolean base;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private TemplateTypeEntity type;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "template_id")
    private List<QuestionCategoryEntity> categories;

    @ManyToOne
    @JoinColumn(name = "auditor_id")
    private AuditorEntity auditor;

    @LastModifiedDate
    private LocalDate modifiedDate;

}
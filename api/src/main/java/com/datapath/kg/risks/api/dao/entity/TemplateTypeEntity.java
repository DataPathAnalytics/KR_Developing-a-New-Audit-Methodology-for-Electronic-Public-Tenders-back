package com.datapath.kg.risks.api.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity(name = "template_type")
public class TemplateTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String nameEn;

    @ElementCollection
    @Column(name = "procurement_method", nullable = false)
    @CollectionTable(name = "template_type_procurement_method",
            joinColumns = {@JoinColumn(name = "type_Id")})
    private List<String> procurementMethods;
}
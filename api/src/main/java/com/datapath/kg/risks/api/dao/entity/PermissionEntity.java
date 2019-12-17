package com.datapath.kg.risks.api.dao.entity;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "permission")
@Data
public class PermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String descriptionEn;
}
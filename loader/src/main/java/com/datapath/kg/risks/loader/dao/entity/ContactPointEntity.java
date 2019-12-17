package com.datapath.kg.risks.loader.dao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Data
@Entity(name="contact_point")
@IdClass(ContactPointId.class)
public class ContactPointEntity {
    @Id
    private Integer id;
    @Id
    private Integer partyId;
    private Integer tenderId;
    private String email;
    private String phone;
}

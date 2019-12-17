package com.datapath.kg.risks.loader.dao.entity.tv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class TenderCpvListIdentity implements Serializable {

    private Integer tenderId;
    private String cpv;

}

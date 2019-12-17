package com.datapath.kg.risks.loader.dao.entity.tv;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Data
@Entity(name = "tender_cpv_list")
@NoArgsConstructor
public class TenderCpvList {

    @EmbeddedId
    private TenderCpvListIdentity identity;

    public TenderCpvList(Integer tenderId, String cpv) {
        identity = new TenderCpvListIdentity(tenderId, cpv);
    }

    public String getCpv() {
        return identity.getCpv();
    }
}

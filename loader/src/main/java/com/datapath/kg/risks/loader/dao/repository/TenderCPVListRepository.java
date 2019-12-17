package com.datapath.kg.risks.loader.dao.repository;

import com.datapath.kg.risks.loader.dao.entity.tv.TenderCpvList;
import com.datapath.kg.risks.loader.dao.entity.tv.TenderCpvListIdentity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TenderCPVListRepository extends CrudRepository<TenderCpvList, TenderCpvListIdentity> {

    List<TenderCpvList> findByIdentityTenderId(Integer tenderId);

}
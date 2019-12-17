package com.datapath.kg.risks.loader.utils;

import com.datapath.kg.risks.loader.dao.entity.ContactPointEntity;
import com.datapath.kg.risks.loader.dao.entity.PartyEntity;
import com.datapath.kg.risks.loader.dao.entity.ReleaseEntity;
import com.datapath.kg.risks.loader.dto.ContactPointDTO;
import com.datapath.kg.risks.loader.dto.PartyDTO;
import com.datapath.kg.risks.loader.dto.ReleaseDTO;

import java.util.List;

public interface Converter {

    PartyEntity convert(PartyDTO partyDTO);

    ReleaseEntity convert(ReleaseDTO releaseDTO);

    List<ContactPointEntity> convert(List<ContactPointDTO> dto);
}

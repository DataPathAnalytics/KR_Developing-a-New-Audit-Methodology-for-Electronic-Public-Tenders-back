package com.datapath.kg.risks.loader.handlers;

import com.datapath.kg.risks.loader.dao.entity.ContactPointEntity;
import com.datapath.kg.risks.loader.dao.entity.PartyEntity;
import com.datapath.kg.risks.loader.dao.entity.ReleaseEntity;
import com.datapath.kg.risks.loader.dao.repository.ReleaseRepository;
import com.datapath.kg.risks.loader.dao.service.ContactPointDAOService;
import com.datapath.kg.risks.loader.dao.service.PartyDAOService;
import com.datapath.kg.risks.loader.dto.PartyDTO;
import com.datapath.kg.risks.loader.dto.ReleaseDTO;
import com.datapath.kg.risks.loader.utils.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Component
@Slf4j
public class ReleasePersistHandler {

    private static final String BUYER = "buyer";
    private static final String SUPPLIER = "supplier";

    @Autowired
    private PartyDAOService partyDAOService;
    @Autowired
    private ReleaseRepository releaseRepository;
    @Autowired
    private ContactPointDAOService contactPointDAOService;
    @Autowired
    private Converter converter;

    @Transactional
    public ReleaseEntity handle(ReleaseDTO releaseDTO) {
        log.info("Save release - {}", releaseDTO.getOcid());

        ReleaseEntity releaseEntity = converter.convert(releaseDTO);

        contactPointDAOService.deleteByTenderId(releaseEntity.getTender().getId());

        List<ContactPointEntity> contactPointEntities = new ArrayList<>();

        if (!isEmpty(releaseDTO.getParties())) {
            for (PartyDTO partyDTO : releaseDTO.getParties()) {
                PartyEntity partyEntity = converter.convert(partyDTO);
                if (!isEmpty(partyDTO.getRoles()) &&
                        (partyDTO.getRoles().contains(BUYER) || partyDTO.getRoles().contains(SUPPLIER))) {

                    Integer buyerId = partyDAOService.save(partyEntity);

                    if (!isEmpty(partyDTO.getAdditionalContactPoints())) {
                        contactPointEntities.addAll(converter.convert(partyDTO.getAdditionalContactPoints()));
                        contactPointEntities.forEach(cp -> cp.setPartyId(buyerId));
                    }

                    if (partyDTO.getRoles().contains(BUYER)) {
                        releaseEntity.getTender().setBuyerId(buyerId);
                    } else if (partyDTO.getRoles().contains(SUPPLIER)) {
                        releaseEntity.getTender().getSuppliers().add(partyEntity);
                    }
                }
            }
        }

        ReleaseEntity release = releaseRepository.save(releaseEntity);

        for (int i = 0; i < contactPointEntities.size(); i++) {
            contactPointEntities.get(i).setId(i + 1);
            contactPointEntities.get(i).setTenderId(releaseEntity.getTender().getId());
        }
        contactPointDAOService.saveAll(contactPointEntities);

        return release;
    }
}
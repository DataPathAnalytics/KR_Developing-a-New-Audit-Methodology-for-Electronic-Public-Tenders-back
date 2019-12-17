package com.datapath.kg.risks.loader;

import com.datapath.kg.risks.loader.dao.entity.*;
import com.datapath.kg.risks.loader.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {LocalDateTimeMapper.class})
public interface DataMapper {

    @Mapping(target = "tender.awards", source = "awards")
    @Mapping(target = "tender.complaints", source = "complaints")
    @Mapping(target = "tender.bids", source = "bids.details")
    @Mapping(target = "tender.contracts", source = "contracts")
    @Mapping(target = "tender", source = "tender")
    @Mapping(target = "tender.amount", source = "tender.value.amount")
    @Mapping(target = "tender.currency", source = "tender.value.currency")
    @Mapping(target = "tender.guaranteeAmount", source = "tender.guarantee.amount")
    @Mapping(target = "tender.periodStartDate", source = "tender.tenderPeriod.startDate")
    @Mapping(target = "tender.periodEndDate", source = "tender.tenderPeriod.endDate")
    ReleaseEntity map(ReleaseDTO dto);

    @Mapping(ignore = true, target = "id")
    @Mapping(target = "outerId", source = "id")
    @Mapping(target = "identifierId", source = "identifier.id")
    @Mapping(target = "identifierScheme", source = "identifier.scheme")
    @Mapping(target = "identifierLegalName", source = "identifier.legalName")
    @Mapping(target = "identifierLegalNameRu", source = "identifier.legalName_ru")
    @Mapping(target = "identifierLegalNameKg", source = "identifier.legalName_kg")
    @Mapping(target = "district", source = "address.district")
    @Mapping(target = "locality", source = "address.locality")
    @Mapping(target = "streetAddress", source = "address.streetAddress")
    @Mapping(target = "region", source = "address.region")
    PartyEntity map(PartyDTO dto);

    @Mapping(target = "valueAmount", source = "value.amount")
    @Mapping(target = "valueCurrency", source = "value.currency")
    LotEntity map(LotDTO dto);

    @Mapping(target = "classificationId", source = "classification.id")
    @Mapping(target = "unitId", source = "unit.id")
    @Mapping(target = "unitName", source = "unit.name")
    @Mapping(target = "unitValueAmount", source = "unit.value.amount")
    @Mapping(target = "unitValueCurrency", source = "unit.value.currency")
    ItemEntity map(ItemDTO dto);

    @Mapping(target = "valueAmount", source = "value.amount")
    @Mapping(target = "valueCurrency", source = "value.currency")
    @Mapping(target = "date", source = "datePublished")
    AwardEntity map(AwardDTO dto);

    @Mapping(target = "relationship", expression = "java(dto.getRelationship().get(0))")
    RelatedProcessEntity map(RelatedProcessDTO dto);

    @Mapping(target = "unitId", source = "unit.id")
    @Mapping(target = "unitName", source = "unit.name")
    @Mapping(target = "unitValueAmount", source = "unit.value.amount")
    @Mapping(target = "unitValueCurrency", source = "unit.value.currency")
    PriceProposalEntity map(PriceProposalDTO dto);

    @Mapping(target = "valueAmount", source = "value.amount")
    ContractEntity map(ContractDTO dto);

    List<ContactPointEntity> map(List<ContactPointDTO> dto);
}

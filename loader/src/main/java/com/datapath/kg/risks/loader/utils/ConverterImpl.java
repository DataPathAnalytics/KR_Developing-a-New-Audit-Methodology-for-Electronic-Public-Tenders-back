package com.datapath.kg.risks.loader.utils;

import com.datapath.kg.risks.loader.DataMapper;
import com.datapath.kg.risks.loader.dao.entity.*;
import com.datapath.kg.risks.loader.dto.ContactPointDTO;
import com.datapath.kg.risks.loader.dto.PartyDTO;
import com.datapath.kg.risks.loader.dto.ReleaseDTO;
import com.datapath.kg.risks.loader.dto.TenderDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.datapath.kg.risks.loader.utils.Constants.ACTIVE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
public class ConverterImpl implements Converter {

    private DataMapper mapper;

    public ConverterImpl(DataMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public PartyEntity convert(PartyDTO partyDTO) {
        return mapper.map(partyDTO);
    }

    @Override
    public ReleaseEntity convert(ReleaseDTO releaseDTO) {
        TenderDTO tenderDTO = releaseDTO.getTender();

        ReleaseEntity releaseEntity = mapper.map(releaseDTO);

        TenderEntity tenderEntity = releaseEntity.getTender();
        tenderEntity.setBadQuality(isBadQuality(releaseDTO));
        tenderEntity.setRelease(releaseEntity);

        if (!tenderEntity.isBadQuality()) {
            tenderEntity.setHasDocuments(!isEmpty(tenderDTO.getDocuments()));
        }

        if (!isEmpty(tenderEntity.getQualificationRequirements())) {
            tenderEntity.getQualificationRequirements()
                    .forEach(qr -> qr.setTender(tenderEntity));
        }

        if (!isEmpty(tenderEntity.getEnquiries())) {
            tenderEntity.getEnquiries()
                    .forEach(e -> e.setTender(tenderEntity));
        }

        if (!isEmpty(tenderEntity.getLots())) {
            tenderEntity.getLots().forEach(lot ->
                    lot.setTender(tenderEntity)
            );
        }

        if (!isEmpty(tenderEntity.getComplaints())) {
            tenderEntity.getComplaints().forEach(complaint ->
                    complaint.setTender(tenderEntity)
            );
        }

        if (!isEmpty(releaseDTO.getRelatedProcesses())) {
            Set<RelatedProcessEntity> relatedProcesses = IntStream
                    .range(0, releaseDTO.getRelatedProcesses().size())
                    .mapToObj(i -> {
                        RelatedProcessEntity entity = mapper.map(releaseDTO.getRelatedProcesses().get(i));
                        entity.setId(i + "-" + tenderEntity.getId());
                        entity.setTender(tenderEntity);
                        return entity;
                    }).collect(Collectors.toSet());
            tenderEntity.setRelatedProcesses(relatedProcesses);
        }

        processItems(tenderEntity, tenderDTO);
        processAwards(releaseEntity, releaseDTO);
        processBids(tenderEntity, releaseDTO);
        processContracts(tenderEntity, releaseDTO);

        return releaseEntity;
    }

    private void processContracts(TenderEntity tenderEntity, ReleaseDTO releaseDTO) {
        if (isEmpty(tenderEntity.getContracts())) return;

        tenderEntity.getContracts().forEach(contract -> contract.setTender(tenderEntity));

//        if (isEmpty(tenderEntity.getAwards())) return;
//        tenderEntity.getContracts().forEach(contract -> releaseDTO.getContracts().stream()
//                .filter(contractDTO -> contractDTO.getId().equals(contract.getId()))
//                .findFirst()
//                .ifPresent(contractDTO -> contractDTO.getAwardIDs()
//                        .forEach(awardId -> tenderEntity.getAwards().stream()
//                                .filter(award -> ((Long) award.getId().longValue()).equals(awardId))
//                                .findFirst()
//                                .ifPresent(award -> {
//                                    award.getContracts().add(contract);
//                                    contract.getAwards().add(award);
//                                }))));
    }

    private void processAwards(ReleaseEntity releaseEntity, ReleaseDTO releaseDTO) {
        TenderEntity tenderEntity = releaseEntity.getTender();
        if (!isEmpty(tenderEntity.getAwards())) {
            tenderEntity.getAwards().forEach(award -> award.setTender(tenderEntity));
        }

        if (!isEmpty(releaseDTO.getAwards())) {

            releaseDTO.getAwards().forEach(awardDTO -> {
                if (!CollectionUtils.isEmpty(awardDTO.getRelatedLots())) {

                    tenderEntity.getAwards().stream().filter(awardEntity -> awardEntity.getId().equals(awardDTO.getId())).findFirst().ifPresent(awardEntity -> {

                        tenderEntity.getLots().stream().filter(lot -> lot.getId().equals(awardDTO.getRelatedLots().get(0))).findFirst().ifPresent(lot -> {
                            lot.getAwards().add(awardEntity);
                            awardEntity.setLot(lot);
                        });

                    });

                }

                if (nonNull(awardDTO.getRelatedBid())) {
                    tenderEntity.getAwards().stream().filter(awardEntity -> awardEntity.getId().equals(awardDTO.getId())).findFirst().ifPresent(awardEntity -> {

                        tenderEntity.getBids().stream().filter(bid -> bid.getId().equals(awardDTO.getRelatedBid()))
                                .findFirst()
                                .ifPresent(awardEntity::setBid);
                    });
                }
            });
        }
    }

    private void processItems(TenderEntity tenderEntity, TenderDTO tenderDTO) {
        if (isEmpty(tenderDTO.getItems())) return;

        tenderEntity.getItems().forEach(item -> item.setTender(tenderEntity));

        tenderDTO.getItems().forEach(itemDTO -> tenderEntity.getItems().stream()
                .filter(itemEntity -> itemEntity.getId().equals(itemDTO.getId()))
                .findFirst()
                .ifPresent(itemEntity -> tenderEntity.getLots().stream().filter(lot -> lot.getId().equals(itemDTO.getRelatedLot())).findFirst().ifPresent(lot -> {
                    lot.getItems().add(itemEntity);
                    itemEntity.setLot(lot);
                })));

    }

    private void processBids(TenderEntity tenderEntity, ReleaseDTO releaseDTO) {
        if (!isEmpty(tenderEntity.getBids())) {
            tenderEntity.getBids().forEach(bid -> bid.setTender(tenderEntity));

            tenderEntity.getBids().forEach(bid -> releaseDTO.getBids().getDetails().stream()
                    .filter(bidDTO -> bid.getId().equals(bidDTO.getId()) && !isEmpty(bidDTO.getRelatedLots()))
                    .findFirst()
                    .ifPresent(bidDTO -> {
                                bid.setBidderId(bidDTO.getTenderers().get(0).getId());
                                bidDTO.getRelatedLots().forEach(relatedLot ->
                                        bid.getBidLots().add(new BidLotEntity(bid.getId(), relatedLot.getId(), relatedLot.getValue().getAmount()))
                                );
                            }
                    ));

            if (isEmpty(tenderEntity.getLots())) return;

            tenderEntity.getBids().forEach(bid -> releaseDTO.getBids().getDetails().stream()
                    .filter(bidDTO -> bid.getId().equals(bidDTO.getId()) && !isEmpty(bidDTO.getPriceProposals()))
                    .findFirst()
                    .ifPresent(bidDTO -> bidDTO.getPriceProposals().forEach(pp -> {
                        PriceProposalEntity proposalEntity = mapper.map(pp);
                        proposalEntity.setBid(bid);

                        if (!isEmpty(tenderEntity.getLots())) {
                            tenderEntity.getLots().stream()
                                    .filter(lot -> lot.getId().equals(pp.getRelatedLot()))
                                    .findFirst()
                                    .ifPresent(lot -> {
                                        lot.getPriceProposal().add(proposalEntity);
                                        proposalEntity.setLot(lot);
                                    });
                        }
                        if (!isEmpty(tenderEntity.getItems())) {
                            tenderEntity.getItems().stream()
                                    .filter(item -> item.getId().equals(pp.getRelatedItem()))
                                    .findFirst()
                                    .ifPresent(item -> {
                                        item.getPriceProposal().add(proposalEntity);
                                        proposalEntity.setItem(item);
                                    });
                        }
                        bid.getPriceProposal().add(proposalEntity);
                    })));
        }
    }

    private boolean isBadQuality(ReleaseDTO releaseDTO) {
        return !isAcceptableTags(releaseDTO) || existManyActiveAwardsOnSameLot(releaseDTO) || hasAwardsWithoutDate(releaseDTO);
    }

    private boolean isAcceptableTags(ReleaseDTO releaseDTO) {
        return releaseDTO.getTag().containsAll(Arrays.asList("tender", "bidsFullOpening", "award"));
    }

    private boolean existManyActiveAwardsOnSameLot(ReleaseDTO releaseDTO) {
        if (isEmpty(releaseDTO.getAwards())) return false;

        return releaseDTO.getAwards()
                .stream()
                .filter(a -> ACTIVE.equalsIgnoreCase(a.getStatus()))
                .filter(a -> !isEmpty(a.getRelatedLots()))
                .map(a -> a.getRelatedLots().get(0))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .anyMatch(lotIdCount -> lotIdCount.getValue() > 1);
    }

    private boolean hasAwardsWithoutDate(ReleaseDTO release) {
        if (isEmpty(release.getAwards())) return false;
        return release.getAwards().stream().anyMatch(award -> isNull(award.getDatePublished()));
    }

    @Override
    public List<ContactPointEntity> convert(List<ContactPointDTO> dto) {
        return mapper.map(dto);
    }
}

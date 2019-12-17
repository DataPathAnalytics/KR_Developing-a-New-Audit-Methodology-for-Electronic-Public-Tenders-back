package com.datapath.kg.risks.loader.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReleaseDTO {
    private String ocid;
    private String date;
    private List<String> tag;
    private List<PartyDTO> parties;
    private TenderDTO tender;
    private List<AwardDTO> awards;
    private List<RelatedProcessDTO> relatedProcesses;
    private BidsDTO bids;
    private List<ComplaintDTO> complaints;
    private List<ContractDTO> contracts;
}

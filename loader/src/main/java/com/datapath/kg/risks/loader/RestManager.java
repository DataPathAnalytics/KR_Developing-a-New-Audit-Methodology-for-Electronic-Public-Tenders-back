package com.datapath.kg.risks.loader;

import com.datapath.kg.risks.loader.dto.ReleaseDTO;
import com.datapath.kg.risks.loader.dto.ReleasesPage;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestManager {

    private RestTemplate restTemplate;
    private boolean isResident;

    public RestManager(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ReleasesPage getReleases(String offset) {
        return restTemplate.getForObject("/releases?size=1000&offset={offset}", ReleasesPage.class, offset);
    }

    public ReleaseDTO getRelease(String ocid) {
        return restTemplate.getForObject("/releases/{ocid}", ReleaseDTO.class, ocid);
    }

}

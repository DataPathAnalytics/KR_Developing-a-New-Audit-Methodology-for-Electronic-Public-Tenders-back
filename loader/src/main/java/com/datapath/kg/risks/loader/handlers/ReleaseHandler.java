package com.datapath.kg.risks.loader.handlers;

import com.datapath.kg.risks.loader.dto.ReleaseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReleaseHandler {

    @Autowired
    private ReleasePersistHandler releasePersistHandler;

    public void handle(ReleaseDTO releaseDTO) {
        releasePersistHandler.handle(releaseDTO);
    }

}
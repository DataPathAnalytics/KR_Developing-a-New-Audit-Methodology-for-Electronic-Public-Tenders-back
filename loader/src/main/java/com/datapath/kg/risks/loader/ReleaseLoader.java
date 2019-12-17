package com.datapath.kg.risks.loader;

import com.datapath.kg.risks.loader.dao.entity.ReleaseEntity;
import com.datapath.kg.risks.loader.dao.service.ReleaseDAOService;
import com.datapath.kg.risks.loader.dto.ReleaseDTO;
import com.datapath.kg.risks.loader.dto.ReleasesPage;
import com.datapath.kg.risks.loader.handlers.ReleaseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static java.util.Objects.nonNull;

@Component
@Slf4j
public class ReleaseLoader {

    private static final ZoneId BISHKEK_ZONE = ZoneId.of("Asia/Bishkek");

    @Autowired
    private ReleaseDAOService releaseDAOService;
    @Autowired
    private RestManager restManager;
    @Autowired
    private ReleaseHandler releaseHandler;

    public void run() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

        ReleasesPage releasesPage;
        do {
            String offset = getLastOffset();
            releasesPage = restManager.getReleases(offset);

            List<Future<ReleaseDTO>> futures = new ArrayList<>();

            for (ReleaseDTO contentRelease : releasesPage.getContent()) {
                Future<ReleaseDTO> future = executor.submit(() -> restManager.getRelease(contentRelease.getOcid()));
                futures.add(future);
            }

            for (Future<ReleaseDTO> future : futures) {
                releaseHandler.handle(future.get());
            }

        } while (!releasesPage.isLast());
    }

    private String getLastOffset() {
        ReleaseEntity release = releaseDAOService.getLastRelease();
        if (nonNull(release)) {
            return ZonedDateTime.of(release.getDate(), BISHKEK_ZONE).format(ISO_OFFSET_DATE_TIME);
        }

        //fix me return null after testing
//        return "2018-01-01 00:00:00.000000";
        return null;
    }

}
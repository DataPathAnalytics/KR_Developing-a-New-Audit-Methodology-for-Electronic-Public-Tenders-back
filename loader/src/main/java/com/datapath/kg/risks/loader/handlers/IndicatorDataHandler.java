package com.datapath.kg.risks.loader.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class IndicatorDataHandler {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void handle() {
        log.info("Updating transaction variables started");
        jdbcTemplate.execute("SELECT update_transaction_variables();");
        log.info("Updating transaction variables finished");

        log.info("Updating analytic tables started");
        jdbcTemplate.execute("SELECT update_analytic_tables();");
        log.info("Updating analytic tables finished");

        log.info("Updating indicators started");
        jdbcTemplate.execute("SELECT update_indicators();");
        log.info("Updating indicators finished");
    }

    public void prioritizationHandle() {
        log.info("Updating prioritization started");
        jdbcTemplate.execute("SELECT update_prioritization();");
        log.info("Updating prioritization finished");
    }
}

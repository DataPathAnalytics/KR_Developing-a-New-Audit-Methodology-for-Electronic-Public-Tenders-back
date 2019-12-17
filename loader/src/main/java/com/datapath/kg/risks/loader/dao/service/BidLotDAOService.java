package com.datapath.kg.risks.loader.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BidLotDAOService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Integer> getBidIdsByLotId(Integer lotId) {
        return jdbcTemplate.queryForList("select bid_id from bid_lot where lot_id = ?", Integer.class, lotId);
    }
}

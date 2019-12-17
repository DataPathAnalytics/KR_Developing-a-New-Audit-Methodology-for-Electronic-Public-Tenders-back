package com.datapath.kg.risks.api.dao.service;

import com.datapath.kg.risks.api.dao.entity.TenderEntity;
import com.datapath.kg.risks.api.dao.entity.TenderIndicatorEntity;
import com.datapath.kg.risks.api.dao.repository.TenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import org.springframework.stereotype.Service;


@Service
public class TenderDAOServiceImpl implements TenderDAOService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TenderRepository tenderRepository;

    @Override
    public TenderEntity getTender(Integer id) {
        return tenderRepository.getOne(id);
    }

    @Override
    public List<TenderIndicatorEntity> getIndicators(Integer tenderId) {
        return jdbcTemplate.query("SELECT i.*, t.indicator_value FROM tender_indicator t\n" +
                "JOIN indicator i on t.indicator_id = i.id\n" +
                "WHERE indicator_value = 1 AND tender_id = ?", new BeanPropertyRowMapper<>(TenderIndicatorEntity.class), tenderId);
    }

}
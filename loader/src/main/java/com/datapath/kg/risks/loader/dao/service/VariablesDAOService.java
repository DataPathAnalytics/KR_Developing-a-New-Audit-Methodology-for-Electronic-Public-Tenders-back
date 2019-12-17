package com.datapath.kg.risks.loader.dao.service;

import com.datapath.kg.risks.loader.dao.entity.tv.ActiveCPV;
import com.datapath.kg.risks.loader.dao.entity.tv.ReportCPV;
import com.datapath.kg.risks.loader.dao.entity.tv.ReportOneTime;
import com.datapath.kg.risks.loader.dao.entity.tv.TenderCpvList;
import com.datapath.kg.risks.loader.dao.repository.TenderCPVListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class VariablesDAOService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TenderCPVListRepository tenderCPVListRepository;

    public List<ReportOneTime> getReportOneTimesByBuyer(Integer buyerId) {
        return jdbcTemplate.query("SELECT * FROM report_one_time WHERE buyer_id = ?", new BeanPropertyRowMapper<>(ReportOneTime.class), buyerId);
    }

    public List<ReportOneTime> getReportOneTimesByBuyerAndYear(Integer buyerId, Integer publishedYear) {
        return jdbcTemplate.query("SELECT * FROM report_one_time WHERE buyer_id = ? AND published_year = ?", new BeanPropertyRowMapper<>(ReportOneTime.class), buyerId, publishedYear);
    }

    public List<ReportCPV> getReportCpvByBuyer(Integer buyerId) {
        return jdbcTemplate.query("SELECT * FROM report_cpv WHERE buyer_id = ?", new BeanPropertyRowMapper<>(ReportCPV.class), buyerId);
    }

    public List<ReportCPV> getReportCpvByBuyerAndYear(Integer buyerId, Integer publishedYear) {
        return jdbcTemplate.query("SELECT * FROM report_cpv WHERE buyer_id = ? AND published_year = ?", new BeanPropertyRowMapper<>(ReportCPV.class), buyerId, publishedYear);
    }

    public List<TenderCpvList> getTenderCpvListByTenderId(Integer tenderId) {
        return tenderCPVListRepository.findByIdentityTenderId(tenderId);
    }

    public List<ActiveCPV> getActiveCpvByBuyerAndYearAndDate(Integer buyerId, Integer year, LocalDateTime date) {
        return jdbcTemplate.query("SELECT * FROM active_cpv WHERE buyer_id = ? AND published_year = ? AND date < ?", new BeanPropertyRowMapper<>(ActiveCPV.class), buyerId, year, date);
    }

    public List<ActiveCPV> getActiveCpvByBuyerAndYear(LocalDateTime tenderDate, Integer buyerId, Integer publishedYear) {
        log.info("REceive active cpv by tender date = {}", tenderDate);
        return jdbcTemplate.query("SELECT * FROM calc_active_cpv(?) WHERE buyer_id = ? AND published_year = ?", new BeanPropertyRowMapper<>(ActiveCPV.class), tenderDate, buyerId, publishedYear);
    }
}
package com.datapath.kg.risks.api.dao.repository;

import com.datapath.kg.risks.api.dao.entity.WorkDateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface WorkDateRepository extends JpaRepository<WorkDateEntity, LocalDate> {

    @Query(value = "select * from work_calendar wc where extract(year from wc.date) = ?", nativeQuery = true)
    List<WorkDateEntity> findByYear(Integer year);
}

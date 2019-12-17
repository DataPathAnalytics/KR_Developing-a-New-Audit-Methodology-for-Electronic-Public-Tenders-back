package com.datapath.kg.risks.api.dao.repository;

import com.datapath.kg.risks.api.dao.entity.QuestionCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionCategoryRepository extends JpaRepository<QuestionCategoryEntity, Integer> {

    List<QuestionCategoryEntity> findByTemplateIdOrderByNumber(Integer id);

}

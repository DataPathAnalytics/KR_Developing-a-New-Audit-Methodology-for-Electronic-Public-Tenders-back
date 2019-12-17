package com.datapath.kg.risks.api.dao.repository;

import com.datapath.kg.risks.api.dao.entity.QuestionEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuestionRepository extends CrudRepository<QuestionEntity, Integer> {

    List<QuestionEntity> findAllByCategoryId(Integer categoryId);

}

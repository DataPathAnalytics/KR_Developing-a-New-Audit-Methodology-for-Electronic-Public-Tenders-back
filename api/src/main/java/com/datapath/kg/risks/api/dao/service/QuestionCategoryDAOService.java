package com.datapath.kg.risks.api.dao.service;

import com.datapath.kg.risks.api.dao.entity.QuestionCategoryEntity;

import java.util.List;
import java.util.Optional;

public interface QuestionCategoryDAOService {

    List<QuestionCategoryEntity> getCategories(Integer templateId);

    QuestionCategoryEntity saveCategory(QuestionCategoryEntity entity);

    void deleteCategory(Integer id);

    Optional<QuestionCategoryEntity> getCategory(Integer id);

    QuestionCategoryEntity getCategoryById(Integer id);

}
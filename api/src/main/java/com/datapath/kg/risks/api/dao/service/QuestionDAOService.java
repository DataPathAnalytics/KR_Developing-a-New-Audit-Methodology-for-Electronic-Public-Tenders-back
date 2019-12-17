package com.datapath.kg.risks.api.dao.service;

import com.datapath.kg.risks.api.dao.entity.ChecklistQuestionEntity;
import com.datapath.kg.risks.api.dao.entity.QuestionEntity;

import java.util.List;
import java.util.Optional;

public interface QuestionDAOService {
    List<QuestionEntity> getQuestions(Integer categoryId);

    Optional<QuestionEntity> getQuestion(Integer id);

    QuestionEntity saveQuestion(QuestionEntity entity);

    ChecklistQuestionEntity saveQuestion(ChecklistQuestionEntity entity);

    void deleteQuestion(Integer id);

}
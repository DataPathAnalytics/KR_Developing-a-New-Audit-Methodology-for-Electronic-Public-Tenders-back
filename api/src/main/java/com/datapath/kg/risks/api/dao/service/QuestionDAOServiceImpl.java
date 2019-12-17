package com.datapath.kg.risks.api.dao.service;

import com.datapath.kg.risks.api.dao.entity.ChecklistQuestionEntity;
import com.datapath.kg.risks.api.dao.entity.QuestionEntity;
import com.datapath.kg.risks.api.dao.repository.ChecklistQuestionRepository;
import com.datapath.kg.risks.api.dao.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionDAOServiceImpl implements QuestionDAOService {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ChecklistQuestionRepository checklistQuestionRepository;

    @Override
    public List<QuestionEntity> getQuestions(Integer categoryId) {
        return questionRepository.findAllByCategoryId(categoryId);
    }

    @Override
    public Optional<QuestionEntity> getQuestion(Integer id) {
        return questionRepository.findById(id);
    }

    @Override
    public QuestionEntity saveQuestion(QuestionEntity entity) {
        return questionRepository.save(entity);
    }

    @Override
    public ChecklistQuestionEntity saveQuestion(ChecklistQuestionEntity entity) {
        return checklistQuestionRepository.save(entity);
    }

    @Override
    public void deleteQuestion(Integer id) {
        questionRepository.deleteById(id);
    }
}

package com.datapath.kg.risks.api.service;

import com.datapath.kg.risks.api.DTOEntityMapper;
import com.datapath.kg.risks.api.dao.entity.QuestionEntity;
import com.datapath.kg.risks.api.dao.service.QuestionDAOService;
import com.datapath.kg.risks.api.dto.QuestionDTO;
import com.datapath.kg.risks.api.request.AddQuestionRequest;
import com.datapath.kg.risks.api.response.QuestionsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionDAOService questionDAOService;
    @Autowired
    private DTOEntityMapper mapper;

    @Override
    public QuestionsResponse getQuestions(Integer categoryId) {
        QuestionsResponse response = new QuestionsResponse();
        questionDAOService.getQuestions(categoryId).forEach(entity -> {
            QuestionDTO dto = mapper.map(entity);
            response.getQuestions().add(dto);
        });
        return response;
    }

    @Override
    public QuestionDTO addQuestion(AddQuestionRequest request) {
        QuestionEntity entity = mapper.map(request);
        return mapper.map(questionDAOService.saveQuestion(entity));
    }

    @Override
    public QuestionDTO updateQuestion(QuestionDTO dto) {
        Optional<QuestionEntity> question = questionDAOService.getQuestion(dto.getId());
        if (question.isPresent()) {
            question.get().setDescription(dto.getDescription());
            question.get().setNumber(dto.getNumber());
            return mapper.map(questionDAOService.saveQuestion(question.get()));
        }
        throw new EntityNotFoundException("Can't find question with such identifier");
    }

    @Override
    public void deleteQuestion(Integer id) {
        questionDAOService.deleteQuestion(id);
    }

}

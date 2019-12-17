package com.datapath.kg.risks.api.service;

import com.datapath.kg.risks.api.dto.QuestionDTO;
import com.datapath.kg.risks.api.request.AddQuestionRequest;
import com.datapath.kg.risks.api.response.QuestionsResponse;

public interface QuestionService {

    QuestionsResponse getQuestions(Integer categoryId);

    QuestionDTO addQuestion(AddQuestionRequest request);

    QuestionDTO updateQuestion(QuestionDTO dto);

    void deleteQuestion(Integer id);

}

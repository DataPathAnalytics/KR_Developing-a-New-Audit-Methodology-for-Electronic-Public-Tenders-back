package com.datapath.kg.risks.api.controller;

import com.datapath.kg.risks.api.dto.QuestionDTO;
import com.datapath.kg.risks.api.request.AddQuestionRequest;
import com.datapath.kg.risks.api.response.QuestionsResponse;
import com.datapath.kg.risks.api.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class QuestionController {

    @Autowired
    private QuestionService service;

    @GetMapping("/templates/categories/{categoryId}/questions")
    public QuestionsResponse getQuestions(@PathVariable Integer categoryId) {
        return service.getQuestions(categoryId);
    }

    @PostMapping("/templates/categories/questions")
    public QuestionDTO addQuestion(@Validated @RequestBody AddQuestionRequest request) {
        return service.addQuestion(request);
    }

    @PutMapping("/templates/categories/questions")
    public QuestionDTO addQuestion(@RequestBody QuestionDTO dto) {
        return service.updateQuestion(dto);
    }

    @DeleteMapping("/templates/categories/questions/{id}")
    public void deleteQuestion(@PathVariable Integer id) {
        service.deleteQuestion(id);
    }

}
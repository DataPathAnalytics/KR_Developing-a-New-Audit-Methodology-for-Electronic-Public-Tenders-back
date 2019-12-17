package com.datapath.kg.risks.api.controller;

import com.datapath.kg.risks.api.service.QuestionCategoryService;
import com.datapath.kg.risks.api.dto.QuestionCategoryDTO;
import com.datapath.kg.risks.api.request.AddQuestionCategoryRequest;
import com.datapath.kg.risks.api.response.QuestionCategoriesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class QuestionCategoryController {

    @Autowired
    private QuestionCategoryService service;

    @GetMapping("/templates/{templateId}/categories")
    public QuestionCategoriesResponse getCategories(@PathVariable Integer templateId) {
        return service.getCategories(templateId);
    }

    @PostMapping("/templates/categories")
    public QuestionCategoryDTO addCategory(@Valid @RequestBody AddQuestionCategoryRequest request) {
        return service.addCategory(request);
    }

    @PutMapping("/templates/categories")
    public QuestionCategoryDTO updateCategory(@Valid @RequestBody QuestionCategoryDTO dto) {
        return service.updateCategory(dto);
    }

    @DeleteMapping(("/templates/categories/{id}"))
    public void deleteCategory(@PathVariable Integer id) {
        service.deleteCategory(id);
    }

}

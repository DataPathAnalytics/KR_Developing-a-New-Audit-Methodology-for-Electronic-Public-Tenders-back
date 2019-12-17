package com.datapath.kg.risks.api.service;

import com.datapath.kg.risks.api.dto.QuestionCategoryDTO;
import com.datapath.kg.risks.api.request.AddQuestionCategoryRequest;
import com.datapath.kg.risks.api.response.QuestionCategoriesResponse;

public interface QuestionCategoryService {

    QuestionCategoriesResponse getCategories(Integer templateId);

    QuestionCategoryDTO addCategory(AddQuestionCategoryRequest request);

    QuestionCategoryDTO updateCategory(QuestionCategoryDTO dto);

    void deleteCategory(Integer id);


}

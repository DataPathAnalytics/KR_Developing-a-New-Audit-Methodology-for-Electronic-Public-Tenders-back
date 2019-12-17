package com.datapath.kg.risks.api.service;

import com.datapath.kg.risks.api.comparators.CategoryComparator;
import com.datapath.kg.risks.api.DTOEntityMapper;
import com.datapath.kg.risks.api.comparators.QuestionComparator;
import com.datapath.kg.risks.api.dao.entity.QuestionCategoryEntity;
import com.datapath.kg.risks.api.dao.service.QuestionCategoryDAOService;
import com.datapath.kg.risks.api.dto.QuestionCategoryDTO;
import com.datapath.kg.risks.api.request.AddQuestionCategoryRequest;
import com.datapath.kg.risks.api.response.QuestionCategoriesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionCategoryServiceImpl implements QuestionCategoryService {

    @Autowired
    private QuestionCategoryDAOService categoryDAO;
    @Autowired
    private DTOEntityMapper mapper;
    @Autowired
    private CategoryComparator categoryComparator;

    @Override
    public QuestionCategoriesResponse getCategories(Integer templateId) {
        List<QuestionCategoryDTO> categories = mapper.mapCategories(categoryDAO.getCategories(templateId));
        categories.sort(categoryComparator);
        categories.forEach(category -> category.getQuestions().sort(new QuestionComparator()));
        return new QuestionCategoriesResponse(categories);
    }

    @Override
    public QuestionCategoryDTO addCategory(AddQuestionCategoryRequest request) {
        QuestionCategoryEntity entity = mapper.map(request);
        entity.setTemplateId(request.getTemplateId());
        return mapper.map(categoryDAO.saveCategory(entity));
    }

    @Override
    public QuestionCategoryDTO updateCategory(QuestionCategoryDTO dto) {
        Optional<QuestionCategoryEntity> category = categoryDAO.getCategory(dto.getId());
        if (category.isPresent()) {
            category.get().setName(dto.getName());
            category.get().setNumber(dto.getNumber());
            return mapper.map(categoryDAO.saveCategory(category.get()));
        }
        throw new EntityNotFoundException("Category with such id not found");
    }

    @Override
    public void deleteCategory(Integer id) {
        categoryDAO.deleteCategory(id);
    }

}
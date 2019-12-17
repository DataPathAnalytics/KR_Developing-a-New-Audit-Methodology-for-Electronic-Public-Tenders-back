package com.datapath.kg.risks.api.dao.service;

import com.datapath.kg.risks.api.dao.entity.QuestionCategoryEntity;
import com.datapath.kg.risks.api.dao.repository.QuestionCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionCategoryDAOServiceImpl implements QuestionCategoryDAOService {

    @Autowired
    private QuestionCategoryRepository repository;

    @Override
    public List<QuestionCategoryEntity> getCategories(Integer templateId) {
        return repository.findByTemplateIdOrderByNumber(templateId);
    }

    @Override
    public QuestionCategoryEntity saveCategory(QuestionCategoryEntity entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteCategory(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<QuestionCategoryEntity> getCategory(Integer id) {
        return repository.findById(id);
    }

    @Override
    public QuestionCategoryEntity getCategoryById(Integer id) {
        return repository.getOne(id);
    }

}
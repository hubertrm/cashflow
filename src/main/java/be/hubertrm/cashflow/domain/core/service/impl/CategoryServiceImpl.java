package be.hubertrm.cashflow.domain.core.service.impl;

import be.hubertrm.cashflow.domain.core.exception.ResourceNotFoundException;
import be.hubertrm.cashflow.domain.core.model.Account;
import be.hubertrm.cashflow.domain.core.model.Category;
import be.hubertrm.cashflow.domain.core.repository.CategoryRepository;
import be.hubertrm.cashflow.domain.core.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryRepository categoryRepository;

    private static final String CATEGORY_NOT_FOUND_MESSAGE = "Category not found for this id :: %s";
    private static final String CATEGORY_NOT_FOUND_BY_NAME_MESSAGE = "Category not found for this name :: %s";

    @Override
    public boolean exists(Long id) { return categoryRepository.findById(id).isPresent(); }

    @Override
    public List<Category> getAll() {
        return categoryRepository.getAll();
    }

    @Override
    public Category getById(Long categoryId) throws ResourceNotFoundException {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException(CATEGORY_NOT_FOUND_MESSAGE, categoryId));
    }

    @Override
    public Category getByName(String name) throws ResourceNotFoundException {
        return categoryRepository.findByName(name).orElseThrow(() ->
                new ResourceNotFoundException(CATEGORY_NOT_FOUND_BY_NAME_MESSAGE, name));
    }

    @Override
    public Long create(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void update(Long categoryId, Category categoryDetails) throws ResourceNotFoundException {
        Category current = categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException(CATEGORY_NOT_FOUND_MESSAGE, categoryId));
        categoryRepository.update(current.getId(), categoryDetails);
    }

    @Override
    public void deleteById(Long categoryId) throws ResourceNotFoundException {
        Optional<Category> optionalEntity = categoryRepository.findById(categoryId);
        if(optionalEntity.isPresent()) {
            categoryRepository.deleteById(categoryId);
        } else {
            throw new ResourceNotFoundException(CATEGORY_NOT_FOUND_MESSAGE, categoryId);
        }
    }
}

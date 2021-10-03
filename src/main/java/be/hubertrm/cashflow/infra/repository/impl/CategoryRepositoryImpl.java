package be.hubertrm.cashflow.infra.repository.impl;

import be.hubertrm.cashflow.domain.model.Category;
import be.hubertrm.cashflow.domain.repository.CategoryRepository;
import be.hubertrm.cashflow.infra.entity.CategoryEntity;
import be.hubertrm.cashflow.infra.repository.JpaCategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    private final JpaCategoryRepository repository;

    public CategoryRepositoryImpl(JpaCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Category> findById(Long categoryId) {
        return repository.findById(categoryId).map(CategoryEntity::fromThis);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return repository.findByName(name).map(CategoryEntity::fromThis);
    }

    @Override
    public List<Category> getAll() {
        return repository.findAll().stream().map(CategoryEntity::fromThis).collect(Collectors.toList());
    }

    @Override
    public Long save(Category category) {
        return repository.save(CategoryEntity.from(category)).getId();
    }

    @Override
    public void update(Long categoryId, Category category) {
        category.setId(categoryId);
        save(category);
    }

    @Override
    public void deleteById(Long categoryId) {
        repository.deleteById(categoryId);
    }
}

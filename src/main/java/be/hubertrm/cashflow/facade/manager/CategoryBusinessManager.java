package be.hubertrm.cashflow.facade.manager;

import be.hubertrm.cashflow.domain.exception.ResourceNotFoundException;
import be.hubertrm.cashflow.domain.service.CategoryService;
import be.hubertrm.cashflow.facade.dto.CategoryDto;
import be.hubertrm.cashflow.facade.mapper.CategoryMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class CategoryBusinessManager {

    @Resource
    private CategoryService categoryService;

    private final CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class);

    public List<CategoryDto> getAllCategories() {
        return categoryMapper.toDtoList(categoryService.getAll());
    }

    public CategoryDto getCategoryById(Long id) throws ResourceNotFoundException {
        return categoryMapper.toDto(categoryService.getById(id));
    }

    public CategoryDto getCategoryByName(String name) throws ResourceNotFoundException {
        return categoryMapper.toDto(categoryService.getByName(name));
    }

    public Long createCategory(CategoryDto categoryDto) {
        return categoryService.create(categoryMapper.toModel(categoryDto));
    }

    public void updateCategory(CategoryDto categoryDto, Long id) throws ResourceNotFoundException {
        categoryService.update(id, categoryMapper.toModel(categoryDto));
    }
    
    public void deleteCategoryById(Long id) throws ResourceNotFoundException {
        categoryService.deleteById(id);
    }
}

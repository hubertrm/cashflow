package be.hubertrm.cashflow.facade.manager;

import be.hubertrm.cashflow.domain.core.exception.ResourceNotFoundException;
import be.hubertrm.cashflow.domain.core.service.CategoryService;
import be.hubertrm.cashflow.facade.dto.CategoryDto;
import be.hubertrm.cashflow.facade.mapper.CategoryMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
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

    public Long createCategory(CategoryDto categoryDto) {
        setDateIfNotSpecified(categoryDto);
        return categoryService.create(categoryMapper.toModel(categoryDto));
    }

    public void updateCategory(Long id, CategoryDto categoryDto) throws ResourceNotFoundException {
        setDateIfNotSpecified(categoryDto);
        categoryService.update(id, categoryMapper.toModel(categoryDto));
    }
    
    public void deleteCategoryById(Long id) throws ResourceNotFoundException {
        categoryService.deleteById(id);
    }

    private void setDateIfNotSpecified(CategoryDto categoryDto) {
        if(categoryDto.getDate() == null) {
            categoryDto.setDate(LocalDate.now());
        }
    }
}

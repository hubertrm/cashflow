package be.hubertrm.cashflow.facade.controller;

import be.hubertrm.cashflow.domain.exception.ResourceNotFoundException;
import be.hubertrm.cashflow.facade.dto.CategoryDto;
import be.hubertrm.cashflow.facade.manager.CategoryBusinessManager;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Resource
    private CategoryBusinessManager categoryBusinessManager;

    @GetMapping("")
    public List<CategoryDto> getAllCategories() {
        return categoryBusinessManager.getAllCategories();
    }

    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable(value = "id") Long categoryId)
            throws ResourceNotFoundException {
        return categoryBusinessManager.getCategoryById(categoryId);
    }

    @PostMapping("")
    public Long createCategory(@RequestBody CategoryDto categoryDto) {
        return categoryBusinessManager.createCategory(categoryDto);
    }

    @PutMapping("/{id}")
    public void updateCategory(@PathVariable(value = "id") Long categoryId, @RequestBody CategoryDto categoryDto)
            throws ResourceNotFoundException {
        categoryBusinessManager.updateCategory(categoryDto, categoryId);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable(value = "id") Long categoryId)
            throws ResourceNotFoundException {
        categoryBusinessManager.deleteCategoryById(categoryId);
    }
}

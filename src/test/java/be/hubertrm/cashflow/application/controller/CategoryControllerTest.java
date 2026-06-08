package be.hubertrm.cashflow.application.controller;

import be.hubertrm.cashflow.application.dto.CategoryDto;
import be.hubertrm.cashflow.application.manager.CategoryBusinessManager;
import be.hubertrm.cashflow.domain.core.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.*;

@SpringBootTest
class CategoryControllerTest {

    @Autowired
    private CategoryController controller;

    @MockitoBean
    private CategoryBusinessManager manager;

    private static final LocalDate NOW = LocalDate.of(2021, 12, 31);

    @Nested
    class getAllCategoriesDesigns {

        @Test
        @DisplayName("getAllCategories returns list of CategoryDto")
        void testGetAllCategories() {
            CategoryDto dto = new CategoryDto(1L, "Food", NOW);
            when(manager.getAllCategories()).thenReturn(List.of(dto));

            List<CategoryDto> result = controller.getAllCategories();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("Food");
        }
    }

    @Nested
    class getCategoryByIdDesigns {

        @Test
        @DisplayName("getCategoryById returns CategoryDto")
        void testGetCategoryById() throws ResourceNotFoundException {
            CategoryDto dto = new CategoryDto(1L, "Food", NOW);
            when(manager.getCategoryById(1L)).thenReturn(dto);

            CategoryDto result = controller.getCategoryById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }
    }

    @Nested
    class createCategoryDesigns {

        @Test
        @DisplayName("createCategory returns id")
        void testCreateCategory() {
            CategoryDto dto = new CategoryDto(null, "NewCategory", null);
            when(manager.createCategory(dto)).thenReturn(1L);

            Long result = controller.createCategory(dto);

            assertThat(result).isEqualTo(1L);
        }
    }

    @Nested
    class createBulkCategoriesDesigns {

        @Test
        @DisplayName("createBulkCategories returns list of ids")
        void testCreateBulkCategories() {
            List<CategoryDto> dtos = List.of(new CategoryDto(null, "C1", null));
            when(manager.createBulkCategories(dtos)).thenReturn(List.of(1L));

            List<Long> result = controller.createBulkCategories(dtos);

            assertThat(result).containsExactly(1L);
        }
    }

    @Nested
    class updateCategoryDesigns {

        @Test
        @DisplayName("updateCategory delegates to manager")
        void testUpdateCategory() throws ResourceNotFoundException {
            CategoryDto dto = new CategoryDto(1L, "Updated", NOW);
            doNothing().when(manager).updateCategory(1L, dto);

            assertThatNoException().isThrownBy(() -> controller.updateCategory(1L, dto));
            verify(manager).updateCategory(1L, dto);
        }
    }

    @Nested
    class deleteCategoryDesigns {

        @Test
        @DisplayName("deleteCategory delegates to manager")
        void testDeleteCategory() throws ResourceNotFoundException {
            doNothing().when(manager).deleteCategoryById(1L);

            assertThatNoException().isThrownBy(() -> controller.deleteCategory(1L));
            verify(manager).deleteCategoryById(1L);
        }
    }
}
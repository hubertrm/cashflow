package be.hubertrm.cashflow.application.manager;

import be.hubertrm.cashflow.domain.core.exception.ResourceNotFoundException;
import be.hubertrm.cashflow.domain.core.model.Category;
import be.hubertrm.cashflow.domain.core.service.CategoryService;
import be.hubertrm.cashflow.application.dto.CategoryDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@SpringBootTest
class CategoryBusinessManagerTest {

    @Autowired
    private CategoryBusinessManager manager;

    @MockBean
    private CategoryService service;

    @Nested
    class addMissingDateDesign {
        @Captor
        ArgumentCaptor<Category> categoryCaptor;

        @Test
        @DisplayName("Given that a categoryDto is missing a date, a default one will be provided")
        void given_categoryDtoToCreate_when_noDate_then_provideOne() {
            CategoryDto actual = new CategoryDto(null, "name", null);

            manager.createCategory(actual);

            verify(service).create(categoryCaptor.capture());
            assertThat(categoryCaptor.getValue().getDate()).isNotNull();
        }

        @Test
        @DisplayName("Given that a categoryDto is missing a date, a default one will be provided")
        void given_categoryDtoToUpdate_when_noDate_then_provideOne() throws ResourceNotFoundException {
            CategoryDto actual = new CategoryDto(1L, "name", null);

            manager.updateCategory(1L, actual);

            verify(service).update(eq(1L), categoryCaptor.capture());
            assertThat(categoryCaptor.getValue().getDate()).isNotNull();
        }

    }
}
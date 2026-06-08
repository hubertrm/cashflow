package be.hubertrm.cashflow.domain.core.service.impl;

import be.hubertrm.cashflow.domain.core.exception.ResourceNotFoundException;
import be.hubertrm.cashflow.domain.core.model.Category;
import be.hubertrm.cashflow.domain.core.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CategoryServiceImplTest {

    @Autowired
    private CategoryServiceImpl service;

    @MockitoBean
    private CategoryRepository repository;

    private static final LocalDate NOW = LocalDate.of(2021, 12, 31);

    @Nested
    class findByNameDesigns {

        @Test
        @DisplayName("findByName returns Optional with category when found")
        void testFindByName() {
            Category expected = new Category(1L, "Food", NOW);
            when(repository.findByName("Food")).thenReturn(Optional.of(expected));

            Optional<Category> actual = service.findByName("Food");

            assertThat(actual).isPresent();
            assertThat(actual.get().getName()).isEqualTo("Food");
        }

        @Test
        @DisplayName("findByName returns empty Optional when not found")
        void testFindByNameNotFound() {
            when(repository.findByName("NonExistent")).thenReturn(Optional.empty());

            Optional<Category> actual = service.findByName("NonExistent");

            assertThat(actual).isEmpty();
        }
    }

    @Nested
    class getByNameDesigns {

        @Test
        @DisplayName("getByName returns category when found")
        void testGetByName() throws ResourceNotFoundException {
            Category expected = new Category(1L, "Food", NOW);
            when(repository.findByName("Food")).thenReturn(Optional.of(expected));

            Category actual = service.getByName("Food");

            assertThat(actual.getName()).isEqualTo("Food");
        }

        @Test
        @DisplayName("getByName throws ResourceNotFoundException when not found")
        void testGetByNameThrows() {
            when(repository.findByName("NonExistent")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.getByName("NonExistent"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("NonExistent");
        }
    }

    @Nested
    class getAllPaginatedDesigns {

        @Test
        @DisplayName("getAll paginated returns Page of Category")
        void testGetAllPaginated() {
            Category category = new Category(1L, "Food", NOW);
            PageRequest pageable = PageRequest.of(0, 10);
            Page<Category> page = new PageImpl<>(List.of(category), pageable, 1);
            when(repository.getAll(pageable)).thenReturn(page);

            Page<Category> result = service.getAll(pageable);

            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0).getName()).isEqualTo("Food");
        }
    }
}
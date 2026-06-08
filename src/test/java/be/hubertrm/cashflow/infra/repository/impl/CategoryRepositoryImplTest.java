package be.hubertrm.cashflow.infra.repository.impl;

import be.hubertrm.cashflow.domain.core.model.Category;
import be.hubertrm.cashflow.infra.entity.CategoryEntity;
import be.hubertrm.cashflow.infra.repository.JpaCategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryRepositoryImplTest {

    @Mock
    private JpaCategoryRepository jpaRepository;

    @InjectMocks
    private CategoryRepositoryImpl repository;

    private static final LocalDate NOW = LocalDate.of(2021, 12, 31);

    @Nested
    class findByIdDesigns {

        @Test
        @DisplayName("findById returns mapped Category when found")
        void testFindById() {
            CategoryEntity entity = new CategoryEntity(1L, "Food", NOW);
            when(jpaRepository.findById(1L)).thenReturn(Optional.of(entity));

            Optional<Category> result = repository.findById(1L);

            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(1L);
            assertThat(result.get().getName()).isEqualTo("Food");
        }

        @Test
        @DisplayName("findById returns empty when not found")
        void testFindByIdNotFound() {
            when(jpaRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<Category> result = repository.findById(99L);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    class findByNameDesigns {

        @Test
        @DisplayName("findByName returns mapped Category when found")
        void testFindByName() {
            CategoryEntity entity = new CategoryEntity(1L, "Food", NOW);
            when(jpaRepository.findByName("Food")).thenReturn(Optional.of(entity));

            Optional<Category> result = repository.findByName("Food");

            assertThat(result).isPresent();
            assertThat(result.get().getName()).isEqualTo("Food");
        }
    }

    @Nested
    class getAllDesigns {

        @Test
        @DisplayName("getAll returns all categories mapped")
        void testGetAll() {
            CategoryEntity entity = new CategoryEntity(1L, "Food", NOW);
            when(jpaRepository.findAll()).thenReturn(List.of(entity));

            List<Category> result = repository.getAll();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("Food");
        }
    }

    @Nested
    class saveDesigns {

        @Test
        @DisplayName("save persists Category and returns id")
        void testSave() {
            Category category = new Category(null, "NewCategory", NOW);
            CategoryEntity savedEntity = new CategoryEntity(1L, "NewCategory", NOW);
            when(jpaRepository.save(any(CategoryEntity.class))).thenReturn(savedEntity);

            Long result = repository.save(category);

            assertThat(result).isEqualTo(1L);
        }
    }

    @Nested
    class saveAllDesigns {

        @Test
        @DisplayName("saveAll persists multiple Categories")
        void testSaveAll() {
            Category cat1 = new Category(null, "C1", NOW);
            Category cat2 = new Category(null, "C2", NOW);
            CategoryEntity entity1 = new CategoryEntity(1L, "C1", NOW);
            CategoryEntity entity2 = new CategoryEntity(2L, "C2", NOW);
            when(jpaRepository.saveAll(anyList())).thenReturn(List.of(entity1, entity2));

            List<Long> result = repository.saveAll(List.of(cat1, cat2));

            assertThat(result).containsExactly(1L, 2L);
        }
    }

    @Nested
    class updateDesigns {

        @Test
        @DisplayName("update sets id and saves")
        void testUpdate() {
            Category category = new Category(null, "Updated", NOW);
            CategoryEntity savedEntity = new CategoryEntity(1L, "Updated", NOW);
            when(jpaRepository.save(any(CategoryEntity.class))).thenReturn(savedEntity);

            repository.update(1L, category);

            verify(jpaRepository).save(any(CategoryEntity.class));
        }
    }

    @Nested
    class deleteByIdDesigns {

        @Test
        @DisplayName("deleteById delegates to JpaRepository")
        void testDeleteById() {
            doNothing().when(jpaRepository).deleteById(1L);

            repository.deleteById(1L);

            verify(jpaRepository).deleteById(1L);
        }
    }
}
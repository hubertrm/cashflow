package be.hubertrm.cashflow.infra.entity;

import be.hubertrm.cashflow.domain.core.model.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryEntityTest {

    private static final LocalDate NOW = LocalDate.of(2021, 12, 31);

    @Nested
    class fromDesigns {

        @Test
        @DisplayName("from converts Category to CategoryEntity")
        void testFrom() {
            Category category = new Category(1L, "Food", NOW);

            CategoryEntity entity = CategoryEntity.from(category);

            assertThat(entity.getId()).isEqualTo(1L);
            assertThat(entity.getName()).isEqualTo("Food");
            assertThat(entity.getDate()).isEqualTo(NOW);
        }

        @Test
        @DisplayName("from handles null id")
        void testFromNullId() {
            Category category = new Category(null, "NewCategory", NOW);

            CategoryEntity entity = CategoryEntity.from(category);

            assertThat(entity.getId()).isNull();
            assertThat(entity.getName()).isEqualTo("NewCategory");
        }
    }

    @Nested
    class fromThisDesigns {

        @Test
        @DisplayName("fromThis converts CategoryEntity to Category")
        void testFromThis() {
            CategoryEntity entity = new CategoryEntity(1L, "Food", NOW);

            Category category = entity.fromThis();

            assertThat(category.getId()).isEqualTo(1L);
            assertThat(category.getName()).isEqualTo("Food");
            assertThat(category.getDate()).isEqualTo(NOW);
        }
    }

    @Nested
    class constructorDesigns {

        @Test
        @DisplayName("RequiredArgsConstructor creates an entity with default values")
        void testRequiredArgsConstructor() {
            CategoryEntity entity = new CategoryEntity();
            assertThat(entity.getId()).isNull();
            assertThat(entity.getName()).isNull();
            assertThat(entity.getDate()).isNull();
        }

        @Test
        @DisplayName("AllArgsConstructor creates an entity with all values")
        void testAllArgsConstructor() {
            CategoryEntity entity = new CategoryEntity(1L, "Health", NOW);
            assertThat(entity.getId()).isEqualTo(1L);
            assertThat(entity.getName()).isEqualTo("Health");
            assertThat(entity.getDate()).isEqualTo(NOW);
        }

        @Test
        @DisplayName("setters work correctly")
        void testSetters() {
            CategoryEntity entity = new CategoryEntity();
            entity.setId(2L);
            entity.setName("Transport");
            entity.setDate(NOW);

            assertThat(entity.getId()).isEqualTo(2L);
            assertThat(entity.getName()).isEqualTo("Transport");
            assertThat(entity.getDate()).isEqualTo(NOW);
        }
    }
}
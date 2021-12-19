package be.hubertrm.cashflow.domain.core.service;

import be.hubertrm.cashflow.domain.core.exception.ResourceNotFoundException;
import be.hubertrm.cashflow.domain.core.model.Category;
import be.hubertrm.cashflow.domain.core.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class CategoryServiceTest {

    @Autowired
    private CategoryService service;

    @MockBean
    private CategoryRepository repository;

    @Nested
    class existsDesigns {
        @Test
        @DisplayName("Test exists Success")
        void testExists() {
            Category category = new Category(1L, "name", LocalDate.of(2021, 12, 31));
            doReturn(Optional.of(category)).when(repository).findById(1L);

            boolean actual = service.exists(1L);

            assertThat(actual).isTrue();
        }
        @Test
        @DisplayName("Test exists Failure")
        void testDoesNotExist() {
            doReturn(Optional.empty()).when(repository).findById(1L);

            boolean actual = service.exists(1L);

            assertThat(actual).isFalse();
        }
    }

    @Nested
    class getByIdDesigns {
        @Test
        @DisplayName("Test getById Success")
        void testGetById() throws ResourceNotFoundException {
            Category expected = new Category(1L, "name", LocalDate.of(2021, 12, 31));
            doReturn(Optional.of(expected)).when(repository).findById(1L);

            Category actual = service.getById(1L);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("Test getById Failure")
        void testGetByIdThrows() {
            doReturn(Optional.empty()).when(repository).findById(1L);

            assertThatThrownBy(() -> service.getById(1L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("1");
        }
    }

    @Nested
    class getByNameDesigns {
        @Test
        @DisplayName("Test getByName Success")
        void testGetByName() throws ResourceNotFoundException {
            Category expected = new Category(1L, "name", LocalDate.of(2021, 12, 31));
            doReturn(Optional.of(expected)).when(repository).findByName("name");

            Category actual = service.getByName("name");

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("Test getByName Failure")
        void testGetByNameThrows() {
            doReturn(Optional.empty()).when(repository).findByName("name");

            assertThatThrownBy(() -> service.getByName("name"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("name");
        }
    }

    @Nested
    class createDesigns {
        @Test
        @DisplayName("Test create Success")
        void testCreate() {
            Category actual = new Category(null, "name", LocalDate.of(2021, 12, 31));
            doReturn(1L).when(repository).save(actual);

            assertThat(service.create(actual)).isEqualTo(1L);
        }

        @Test
        @DisplayName("Test create bulk Success")
        void testCreateBulk() {
            List<Category> actual = List.of(
                    new Category(null, "name 1", LocalDate.of(2021, 12, 31)),
                    new Category(null, "name 2", LocalDate.of(2021, 12, 31))
            );
            List<Long> expected = List.of(1L, 2L);
            doReturn(expected).when(repository).saveAll(actual);

            assertThat(service.createAll(actual)).isEqualTo(expected);
        }

        @ParameterizedTest
        @ValueSource(longs = {-1L, 0L, 1L, 10L})
        @DisplayName("Test create with id Success")
        void testCreateWithId(Long id) {
            Category actual = new Category(id, "name", LocalDate.of(2021, 12, 31));
            doReturn(1L).when(repository).save(actual);

            assertThat(service.create(actual)).isEqualTo(1L);
        }
    }

    @Nested
    class updateDesign {
        @Test
        @DisplayName("Test update Success")
        void testUpdate() {
            Category current = new Category(1L, "name", LocalDate.of(2021, 12, 31));
            Category actual = new Category(1L, "name updated", LocalDate.of(2021, 1, 1));
            doReturn(Optional.of(current)).when(repository).findById(1L);

            assertThatNoException().isThrownBy(() -> service.update(1L, actual));
        }

        @Test
        @DisplayName("Test update Failure")
        void testUpdateThrows() {
            Category actual = new Category(1L, "name updated", LocalDate.of(2021, 1, 1));
            doReturn(Optional.empty()).when(repository).findById(1L);

            assertThatThrownBy(() -> service.update(1L, actual))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("1");
        }
    }

    @Nested
    class deleteDesign {
        @Test
        @DisplayName("Test delete Success")
        void testDelete() {
            Category expected = new Category(1L, "name", LocalDate.of(2021, 12, 31));
            doReturn(Optional.of(expected)).when(repository).findById(1L);

            assertThatNoException().isThrownBy(() -> service.deleteById(1L));
        }

        @Test
        @DisplayName("Test delete Failure")
        void testDeleteFailure() {
            assertThatThrownBy(() -> service.deleteById(1L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("1");
        }
    }

}
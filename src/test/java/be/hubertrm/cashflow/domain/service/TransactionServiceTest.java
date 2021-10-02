package be.hubertrm.cashflow.domain.service;

import be.hubertrm.cashflow.domain.exception.ResourceNotFoundException;
import be.hubertrm.cashflow.domain.model.Account;
import be.hubertrm.cashflow.domain.model.Category;
import be.hubertrm.cashflow.domain.model.Transaction;
import be.hubertrm.cashflow.domain.repository.TransactionRepository;
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
class TransactionServiceTest {
    
    @Autowired
    private TransactionService service;

    @MockBean
    private TransactionRepository repository;

    @Nested
    class getByIdDesigns {
        @Test
        @DisplayName("Test getById Success")
        void testGetById() throws ResourceNotFoundException {
            Transaction expected = new Transaction(
                    1L,
                    LocalDate.of(2021, 12, 31),
                    1L,
                    new Category(1L, "name", LocalDate.of(2021, 12, 31)),
                    new Account(1L, "name", LocalDate.of(2021, 12, 31)),
                    "description");
            doReturn(Optional.of(expected)).when(repository).findById(1L);

            Transaction actual = service.getById(1L);

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
    class createDesigns {
        @Test
        @DisplayName("Test create Success")
        void testCreate() {
            Transaction actual = new Transaction(
                    1L,
                    LocalDate.of(2021, 12, 31),
                    1L,
                    new Category(1L, "name", LocalDate.of(2021, 12, 31)),
                    new Account(1L, "name", LocalDate.of(2021, 12, 31)),
                    "description");
            doReturn(1L).when(repository).save(actual);

            assertThat(service.create(actual)).isEqualTo(1L);
        }

        @ParameterizedTest
        @ValueSource(longs = {-1L, 0L, 1L, 10L})
        @DisplayName("Test create with id Success")
        void testCreateWithId(Long id) {
            Transaction actual = new Transaction(
                    id,
                    LocalDate.of(2021, 12, 31),
                    1L,
                    new Category(id, "name", LocalDate.of(2021, 12, 31)),
                    new Account(id, "name", LocalDate.of(2021, 12, 31)),
                    "description");
            doReturn(id).when(repository).save(actual);

            assertThat(service.create(actual)).isEqualTo(id);
        }
    }

    @Nested
    class createMultipleDesigns {
        @Test
        @DisplayName("Test create multiple Success")
        void testCreate() {
            List<Transaction> actual = List.of(new Transaction(
                    null,
                    LocalDate.of(2021, 12, 31),
                    1L,
                    new Category(1L, "name", LocalDate.of(2021, 12, 31)),
                    new Account(1L, "name", LocalDate.of(2021, 12, 31)),
                    "description"));
            doReturn(List.of(1L)).when(repository).save(actual);

            assertThat(service.create(actual)).isEqualTo(List.of(1L));
        }

        @ParameterizedTest
        @ValueSource(longs = {-1L, 0L, 1L, 10L})
        @DisplayName("Test create multiple with id Success")
        void testCreateWithId(Long id) {
            List<Transaction> actual = List.of(new Transaction(
                    id,
                    LocalDate.of(2021, 12, 31),
                    1L,
                    new Category(1L, "name", LocalDate.of(2021, 12, 31)),
                    new Account(1L, "name", LocalDate.of(2021, 12, 31)),
                    "description"));
            doReturn(List.of(id)).when(repository).save(actual);

            assertThat(service.create(actual)).isEqualTo(List.of(id));
        }
    }

    @Nested
    class updateDesign {
        @Test
        @DisplayName("Test update Success")
        void testUpdate() {
            Category category = new Category(1L, "name", LocalDate.of(2021, 12, 31));
            Account account = new Account(1L, "name", LocalDate.of(2021, 12, 31));
            Transaction current = new Transaction(
                    1L,
                    LocalDate.of(2021, 1, 1),
                    1L,
                    category,
                    account,
                    "description");
            Transaction actual = new Transaction(
                    1L,
                    LocalDate.of(2021, 12, 31),
                    2L,
                    category,
                    account,
                    "new description");
            doReturn(Optional.of(current)).when(repository).findById(1L);

            assertThatNoException().isThrownBy(() -> service.update(1L, actual));
        }

        @Test
        @DisplayName("Test update Failure")
        void testUpdateThrows() {
            Transaction actual = new Transaction(
                    1L,
                    LocalDate.of(2021, 12, 31),
                    1L,
                    new Category(1L, "name", LocalDate.of(2021, 12, 31)),
                    new Account(1L, "name", LocalDate.of(2021, 12, 31)),
                    "description");
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
            assertThatNoException().isThrownBy(() -> service.deleteById(1L));
        }
    }
}
package be.hubertrm.cashflow.infra.repository.impl;

import be.hubertrm.cashflow.domain.core.model.Account;
import be.hubertrm.cashflow.domain.core.model.Category;
import be.hubertrm.cashflow.domain.core.model.Transaction;
import be.hubertrm.cashflow.infra.entity.AccountEntity;
import be.hubertrm.cashflow.infra.entity.CategoryEntity;
import be.hubertrm.cashflow.infra.entity.TransactionEntity;
import be.hubertrm.cashflow.infra.repository.JpaTransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionRepositoryImplTest {

    @Mock
    private JpaTransactionRepository jpaRepository;

    @InjectMocks
    private TransactionRepositoryImpl repository;

    private static final LocalDate NOW = LocalDate.of(2021, 12, 31);
    private static final Category CATEGORY = new Category(1L, "Food", NOW);
    private static final Account ACCOUNT = new Account(1L, "Checking", NOW);

    @Nested
    class findByIdDesigns {

        @Test
        @DisplayName("findById returns mapped Transaction when found")
        void testFindById() {
            TransactionEntity entity = createBasicEntity();
            when(jpaRepository.findById(1L)).thenReturn(Optional.of(entity));

            Optional<Transaction> result = repository.findById(1L);

            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(1L);
            assertThat(result.get().getDescription()).isEqualTo("test");
        }

        @Test
        @DisplayName("findById returns empty when not found")
        void testFindByIdNotFound() {
            when(jpaRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<Transaction> result = repository.findById(99L);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    class getAllDesigns {

        @Test
        @DisplayName("getAll returns all transactions mapped")
        void testGetAll() {
            TransactionEntity entity = createBasicEntity();
            when(jpaRepository.findAll()).thenReturn(List.of(entity));

            List<Transaction> result = repository.getAll();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("getAll paginated returns Page of Transaction")
        void testGetAllPaginated() {
            TransactionEntity entity = createBasicEntity();
            PageRequest pageable = PageRequest.of(0, 10);
            Page<TransactionEntity> jpaPage = new PageImpl<>(List.of(entity), pageable, 1);
            when(jpaRepository.findAll(pageable)).thenReturn(jpaPage);

            Page<Transaction> result = repository.getAll(pageable);

            assertThat(result.getContent()).hasSize(1);
        }
    }

    @Nested
    class saveDesigns {

        @Test
        @DisplayName("save persists Transaction and returns id")
        void testSave() {
            Transaction tx = new Transaction(null, NOW, 100f, CATEGORY, ACCOUNT, "test");
            TransactionEntity savedEntity = createBasicEntity();
            when(jpaRepository.save(any(TransactionEntity.class))).thenReturn(savedEntity);

            Long result = repository.save(tx);

            assertThat(result).isEqualTo(1L);
        }
    }

    @Nested
    class saveAllDesigns {

        @Test
        @DisplayName("saveAll persists multiple Transactions")
        void testSaveAll() {
            Transaction tx1 = new Transaction(null, NOW, 100f, CATEGORY, ACCOUNT, "tx1");
            Transaction tx2 = new Transaction(null, NOW, 200f, CATEGORY, ACCOUNT, "tx2");
            TransactionEntity entity1 = new TransactionEntity();
            entity1.setId(1L);
            TransactionEntity entity2 = new TransactionEntity();
            entity2.setId(2L);
            when(jpaRepository.saveAll(anyList())).thenReturn(List.of(entity1, entity2));

            List<Long> result = repository.saveAll(List.of(tx1, tx2));

            assertThat(result).containsExactly(1L, 2L);
        }
    }

    @Nested
    class updateDesigns {

        @Test
        @DisplayName("update sets id and saves")
        void testUpdate() {
            Transaction tx = new Transaction(null, NOW, 100f, CATEGORY, ACCOUNT, "updated");
            TransactionEntity savedEntity = createBasicEntity();
            when(jpaRepository.save(any(TransactionEntity.class))).thenReturn(savedEntity);

            repository.update(1L, tx);

            verify(jpaRepository).save(any(TransactionEntity.class));
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

    @Nested
    class findDuplicateDesigns {

        @Test
        @DisplayName("findDuplicate returns mapped Transaction when found")
        void testFindDuplicate() {
            TransactionEntity entity = createBasicEntity();
            when(jpaRepository.findDuplicate(NOW, 100f, 1L, 1L, "test", null))
                    .thenReturn(Optional.of(entity));

            Optional<Transaction> result = repository.findDuplicate(NOW, 100f, 1L, 1L, "test", null);

            assertThat(result).isPresent();
        }

        @Test
        @DisplayName("findDuplicate returns empty when not found")
        void testFindDuplicateNotFound() {
            when(jpaRepository.findDuplicate(NOW, 100f, 1L, 1L, "test", null))
                    .thenReturn(Optional.empty());

            Optional<Transaction> result = repository.findDuplicate(NOW, 100f, 1L, 1L, "test", null);

            assertThat(result).isEmpty();
        }
    }

    private TransactionEntity createBasicEntity() {
        TransactionEntity entity = new TransactionEntity();
        entity.setId(1L);
        entity.setDate(NOW);
        entity.setAmount(100f);
        entity.setCategory(CategoryEntity.from(CATEGORY));
        entity.setAccount(AccountEntity.from(ACCOUNT));
        entity.setDescription("test");
        return entity;
    }
}
package be.hubertrm.cashflow.infra.repository.impl;

import be.hubertrm.cashflow.domain.core.model.Account;
import be.hubertrm.cashflow.infra.entity.AccountEntity;
import be.hubertrm.cashflow.infra.repository.JpaAccountRepository;
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
class AccountRepositoryImplTest {

    @Mock
    private JpaAccountRepository jpaRepository;

    @InjectMocks
    private AccountRepositoryImpl repository;

    private static final LocalDate NOW = LocalDate.of(2021, 12, 31);

    @Nested
    class findByIdDesigns {

        @Test
        @DisplayName("findById returns mapped Account when found")
        void testFindById() {
            AccountEntity entity = new AccountEntity(1L, "Checking", NOW);
            when(jpaRepository.findById(1L)).thenReturn(Optional.of(entity));

            Optional<Account> result = repository.findById(1L);

            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(1L);
            assertThat(result.get().getName()).isEqualTo("Checking");
        }

        @Test
        @DisplayName("findById returns empty when not found")
        void testFindByIdNotFound() {
            when(jpaRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<Account> result = repository.findById(99L);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    class findByNameDesigns {

        @Test
        @DisplayName("findByName returns mapped Account when found")
        void testFindByName() {
            AccountEntity entity = new AccountEntity(1L, "Checking", NOW);
            when(jpaRepository.findByName("Checking")).thenReturn(Optional.of(entity));

            Optional<Account> result = repository.findByName("Checking");

            assertThat(result).isPresent();
            assertThat(result.get().getName()).isEqualTo("Checking");
        }
    }

    @Nested
    class getAllDesigns {

        @Test
        @DisplayName("getAll returns all accounts mapped")
        void testGetAll() {
            AccountEntity entity = new AccountEntity(1L, "Checking", NOW);
            when(jpaRepository.findAll()).thenReturn(List.of(entity));

            List<Account> result = repository.getAll();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("Checking");
        }

        @Test
        @DisplayName("getAll paginated returns Page of Account")
        void testGetAllPaginated() {
            AccountEntity entity = new AccountEntity(1L, "Checking", NOW);
            PageRequest pageable = PageRequest.of(0, 10);
            Page<AccountEntity> jpaPage = new PageImpl<>(List.of(entity), pageable, 1);
            when(jpaRepository.findAll(pageable)).thenReturn(jpaPage);

            Page<Account> result = repository.getAll(pageable);

            assertThat(result.getContent()).hasSize(1);
        }
    }

    @Nested
    class saveDesigns {

        @Test
        @DisplayName("save persists Account and returns id")
        void testSave() {
            Account account = new Account(null, "NewAccount", NOW);
            AccountEntity savedEntity = new AccountEntity(1L, "NewAccount", NOW);
            when(jpaRepository.save(any(AccountEntity.class))).thenReturn(savedEntity);

            Long result = repository.save(account);

            assertThat(result).isEqualTo(1L);
        }
    }

    @Nested
    class saveAllDesigns {

        @Test
        @DisplayName("saveAll persists multiple Accounts")
        void testSaveAll() {
            Account account1 = new Account(null, "A1", NOW);
            Account account2 = new Account(null, "A2", NOW);
            AccountEntity entity1 = new AccountEntity(1L, "A1", NOW);
            AccountEntity entity2 = new AccountEntity(2L, "A2", NOW);
            when(jpaRepository.saveAll(anyList())).thenReturn(List.of(entity1, entity2));

            List<Long> result = repository.saveAll(List.of(account1, account2));

            assertThat(result).containsExactly(1L, 2L);
        }
    }

    @Nested
    class updateDesigns {

        @Test
        @DisplayName("update sets id and saves")
        void testUpdate() {
            Account account = new Account(null, "Updated", NOW);
            AccountEntity savedEntity = new AccountEntity(1L, "Updated", NOW);
            when(jpaRepository.save(any(AccountEntity.class))).thenReturn(savedEntity);

            repository.update(1L, account);

            verify(jpaRepository).save(any(AccountEntity.class));
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
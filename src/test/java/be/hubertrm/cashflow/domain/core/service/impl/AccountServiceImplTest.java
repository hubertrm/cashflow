package be.hubertrm.cashflow.domain.core.service.impl;

import be.hubertrm.cashflow.domain.core.exception.ResourceNotFoundException;
import be.hubertrm.cashflow.domain.core.model.Account;
import be.hubertrm.cashflow.domain.core.repository.AccountRepository;
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
class AccountServiceImplTest {

    @Autowired
    private AccountServiceImpl service;

    @MockitoBean
    private AccountRepository repository;

    private static final LocalDate NOW = LocalDate.of(2021, 12, 31);

    @Nested
    class findByNameDesigns {

        @Test
        @DisplayName("findByName returns Optional with account when found")
        void testFindByName() {
            Account expected = new Account(1L, "Checking", NOW);
            when(repository.findByName("Checking")).thenReturn(Optional.of(expected));

            Optional<Account> actual = service.findByName("Checking");

            assertThat(actual).isPresent();
            assertThat(actual.get().getName()).isEqualTo("Checking");
        }

        @Test
        @DisplayName("findByName returns empty Optional when not found")
        void testFindByNameNotFound() {
            when(repository.findByName("NonExistent")).thenReturn(Optional.empty());

            Optional<Account> actual = service.findByName("NonExistent");

            assertThat(actual).isEmpty();
        }
    }

    @Nested
    class getByNameDesigns {

        @Test
        @DisplayName("getByName returns account when found - note: throws because AccountService does not have getByName")
        void testGetByName() throws ResourceNotFoundException {
            Account expected = new Account(1L, "Checking", NOW);
            when(repository.findByName("Checking")).thenReturn(Optional.of(expected));

            // AccountService extends Named<Account> which has getByName
            Account actual = service.getByName("Checking");

            assertThat(actual.getName()).isEqualTo("Checking");
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
        @DisplayName("getAll paginated returns Page of Account")
        void testGetAllPaginated() {
            Account account = new Account(1L, "Checking", NOW);
            PageRequest pageable = PageRequest.of(0, 10);
            Page<Account> page = new PageImpl<>(List.of(account), pageable, 1);
            when(repository.getAll(pageable)).thenReturn(page);

            Page<Account> result = service.getAll(pageable);

            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0).getName()).isEqualTo("Checking");
        }
    }
}
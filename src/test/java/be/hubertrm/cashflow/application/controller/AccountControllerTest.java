package be.hubertrm.cashflow.application.controller;

import be.hubertrm.cashflow.application.dto.AccountDto;
import be.hubertrm.cashflow.application.manager.AccountBusinessManager;
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
class AccountControllerTest {

    @Autowired
    private AccountController controller;

    @MockitoBean
    private AccountBusinessManager manager;

    private static final LocalDate NOW = LocalDate.of(2021, 12, 31);

    @Nested
    class getAllAccountsDesigns {

        @Test
        @DisplayName("getAllAccounts returns list of AccountDto")
        void testGetAllAccounts() {
            AccountDto dto = new AccountDto(1L, "Checking", NOW);
            when(manager.getAllAccounts()).thenReturn(List.of(dto));

            List<AccountDto> result = controller.getAllAccounts();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("Checking");
        }
    }

    @Nested
    class getAccountByIdDesigns {

        @Test
        @DisplayName("getAccountById returns AccountDto")
        void testGetAccountById() throws ResourceNotFoundException {
            AccountDto dto = new AccountDto(1L, "Checking", NOW);
            when(manager.getAccountById(1L)).thenReturn(dto);

            AccountDto result = controller.getAccountById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }
    }

    @Nested
    class createAccountDesigns {

        @Test
        @DisplayName("createAccount returns id")
        void testCreateAccount() {
            AccountDto dto = new AccountDto(null, "NewAccount", null);
            when(manager.createAccount(dto)).thenReturn(1L);

            Long result = controller.createAccount(dto);

            assertThat(result).isEqualTo(1L);
        }
    }

    @Nested
    class createBulkAccountsDesigns {

        @Test
        @DisplayName("createBulkAccounts returns list of ids")
        void testCreateBulkAccounts() {
            List<AccountDto> dtos = List.of(new AccountDto(null, "A1", null));
            when(manager.createBulkAccounts(dtos)).thenReturn(List.of(1L));

            List<Long> result = controller.createBulkAccounts(dtos);

            assertThat(result).containsExactly(1L);
        }
    }

    @Nested
    class updateAccountDesigns {

        @Test
        @DisplayName("updateAccount delegates to manager")
        void testUpdateAccount() throws ResourceNotFoundException {
            AccountDto dto = new AccountDto(1L, "Updated", NOW);
            doNothing().when(manager).updateAccount(1L, dto);

            assertThatNoException().isThrownBy(() -> controller.updateAccount(1L, dto));
            verify(manager).updateAccount(1L, dto);
        }
    }

    @Nested
    class deleteAccountByIdDesigns {

        @Test
        @DisplayName("deleteAccountById delegates to manager")
        void testDeleteAccountById() throws ResourceNotFoundException {
            doNothing().when(manager).deleteAccountById(1L);

            assertThatNoException().isThrownBy(() -> controller.deleteAccountById(1L));
            verify(manager).deleteAccountById(1L);
        }
    }
}
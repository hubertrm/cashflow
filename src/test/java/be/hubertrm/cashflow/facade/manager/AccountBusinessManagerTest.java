package be.hubertrm.cashflow.facade.manager;

import be.hubertrm.cashflow.domain.exception.ResourceNotFoundException;
import be.hubertrm.cashflow.domain.model.Account;
import be.hubertrm.cashflow.domain.service.AccountService;
import be.hubertrm.cashflow.facade.dto.AccountDto;
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
class AccountBusinessManagerTest {

    @Autowired
    private AccountBusinessManager manager;

    @MockBean
    private AccountService service;

    @Nested
    class addMissingDateDesign {
        @Captor
        ArgumentCaptor<Account> accountCaptor;

        @Test
        @DisplayName("Given that a accountDto is missing a date, a default one will be provided")
        void given_accountDtoToCreate_when_noDate_then_provideOne() {
            AccountDto actual = new AccountDto(null, "name", null);

            manager.createAccount(actual);

            verify(service).create(accountCaptor.capture());
            assertThat(accountCaptor.getValue().getDate()).isNotNull();
        }

        @Test
        @DisplayName("Given that a accountDto is missing a date, a default one will be provided")
        void given_accountDtoToUpdate_when_noDate_then_provideOne() throws ResourceNotFoundException {
            AccountDto actual = new AccountDto(1L, "name", null);

            manager.updateAccount(1L, actual);

            verify(service).update(eq(1L), accountCaptor.capture());
            assertThat(accountCaptor.getValue().getDate()).isNotNull();
        }

    }
}
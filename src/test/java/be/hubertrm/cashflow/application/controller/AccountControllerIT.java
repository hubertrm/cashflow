package be.hubertrm.cashflow.application.controller;

import be.hubertrm.cashflow.CashflowBaseIntegrationTest;
import be.hubertrm.cashflow.application.dto.CategoryDto;
import be.hubertrm.cashflow.domain.core.model.Account;
import be.hubertrm.cashflow.domain.core.repository.AccountRepository;
import be.hubertrm.cashflow.application.dto.AccountDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AccountControllerIT extends CashflowBaseIntegrationTest {

    public static final String ACCOUNTS_PATH = API_PATH + "/accounts";

    @Autowired
    private JacksonTester<AccountDto> jsonAccount;
    @Autowired
    private JacksonTester<List<AccountDto>> jsonAccountList;
    @Autowired
    private AccountRepository repository;

    @Test
    void given_noEntityExists_when_getAll_then_emptyList() throws Exception {
        mvc.perform(get(ACCOUNTS_PATH))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonAccountList.write(Collections.emptyList()).getJson()));
    }

    @Test
    @Sql("classpath:integrationTest/createAccount.sql")
    void given_multipleEntitiesExist_when_getAll_then_returnsAll() throws Exception {
        AccountDto expected = new AccountDto(1L, "name", LocalDate.of(2021, 1, 1));

        MockHttpServletResponse response = mvc.perform(get(ACCOUNTS_PATH))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(jsonAccountList.parse(response.getContentAsString()).getObject())
                .usingElementComparatorIgnoringFields("id").contains(expected);
    }

    @Test
    @Sql("classpath:integrationTest/createAccount.sql")
    void given_entityExists_when_getById_then_returnsEntity() throws Exception {
        Long id = 1L;
        AccountDto expected = new AccountDto(id, "name", LocalDate.of(2021, 1, 1));

        mvc.perform(get(ACCOUNTS_PATH + "/" + id))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonAccount.write(expected).getJson()));
    }

    @Test
    void given_entityDoesNotExist_when_getById_then_NotFound() throws Exception {
        mvc.perform(get(ACCOUNTS_PATH + "/" + 1L))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void given_entityToCreate_when_create_then_returnsEntityId() throws Exception {
        AccountDto actual = new AccountDto(null, "name", LocalDate.of(2021, 1, 1));

        mvc.perform(post(ACCOUNTS_PATH)
                        .content(jsonAccount.write(actual).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNumber());
    }

    @Test
    void given_entitiesToCreate_when_create_then_returnsEntitiesId() throws Exception {
        List<AccountDto> toCreate = List.of(new AccountDto(null, "name", LocalDate.of(2021, 1, 1)));

        mvc.perform(post(ACCOUNTS_PATH + "/bulk")
                        .content(jsonAccountList.write(toCreate).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0]").isNumber());
    }

    @Test
    @Sql("classpath:integrationTest/createAccount.sql")
    void given_entityExists_when_update_then_update() throws Exception {
        Long id = 1L;
        AccountDto updated = new AccountDto(id, "name updated", LocalDate.of(2021, 1, 1));
        Account expected = new Account(id, "name updated", LocalDate.of(2021, 1, 1));

        mvc.perform(put(ACCOUNTS_PATH + "/" + id)
                        .content(jsonAccount.write(updated).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        assertThat(repository.findById(id))
                .isPresent().contains(expected);
    }

    @Test
    void given_entityDoesNotExist_when_update_then_NotFound() throws Exception {
        AccountDto updated = new AccountDto(1L, "name updated", LocalDate.of(2021, 1, 1));

        mvc.perform(put(ACCOUNTS_PATH + "/" + 1L)
                        .content(jsonAccount.write(updated).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        assertThat(repository.findById(1L)).isNotPresent();
    }

    @Test
    @Sql("classpath:integrationTest/createAccount.sql")
    void given_entityExists_when_deleted_then_doesNoLongerExist() throws Exception {
        Long id = 1L;
        mvc.perform(delete(ACCOUNTS_PATH + "/" + id))
                .andExpect(status().is2xxSuccessful());

        assertThat(repository.findById(id)).isNotPresent();
    }
}
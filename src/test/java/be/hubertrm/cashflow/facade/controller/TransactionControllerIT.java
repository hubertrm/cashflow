package be.hubertrm.cashflow.facade.controller;

import be.hubertrm.cashflow.CashflowBaseIntegrationTest;
import be.hubertrm.cashflow.domain.model.Account;
import be.hubertrm.cashflow.domain.model.Category;
import be.hubertrm.cashflow.domain.model.Transaction;
import be.hubertrm.cashflow.domain.repository.TransactionRepository;
import be.hubertrm.cashflow.facade.dto.AccountDto;
import be.hubertrm.cashflow.facade.dto.CategoryDto;
import be.hubertrm.cashflow.facade.dto.TransactionDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TransactionControllerIT extends CashflowBaseIntegrationTest {

    public static final String TRANSACTIONS_PATH = API_PATH + "/transactions";

    @Autowired
    private JacksonTester<TransactionDto> jsonTransaction;
    @Autowired
    private JacksonTester<List<TransactionDto>> jsonTransactionList;
    @Autowired
    private TransactionRepository repository;

    @Test
    void given_noEntityExists_when_getAll_then_emptyList() throws Exception {
        mvc.perform(get(TRANSACTIONS_PATH))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonTransactionList.write(Collections.emptyList()).getJson()));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = "classpath:integrationTest/createAll.sql")
    void given_multipleEntitiesExist_when_getAll_then_returnsAll() throws Exception {
        TransactionDto expected = createTransactionDto(1L);

        MockHttpServletResponse response = mvc.perform(get(TRANSACTIONS_PATH))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(jsonTransactionList.parse(response.getContentAsString()).getObject())
                .usingElementComparatorIgnoringFields("id").contains(expected);
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = "classpath:integrationTest/createAll.sql")
    void given_entityExists_when_getById_then_returnsEntity() throws Exception {
        TransactionDto expected = createTransactionDto(1L);

        mvc.perform(get(TRANSACTIONS_PATH + "/" + 1L))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonTransaction.write(expected).getJson()));
    }

    @Test
    void given_entityDoesNotExist_when_getById_then_NotFound() throws Exception {
        mvc.perform(get(TRANSACTIONS_PATH + "/" + 999L))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                    scripts = "classpath:integrationTest/createCategory.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                    scripts = "classpath:integrationTest/createAccount.sql"),
    })
    void given_entityToCreate_when_create_then_returnsEntityId() throws Exception {
        TransactionDto actual = createTransactionDto(null);

        mvc.perform(post(TRANSACTIONS_PATH)
                        .content(jsonTransaction.write(actual).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNumber());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = "classpath:integrationTest/createAccount.sql")
    void given_categoryDoesNotExist_when_create_then_fail() throws Exception {
        TransactionDto actual = createTransactionDto(null)
                .setCategory(new CategoryDto(999L, "name", LocalDate.of(2021, 1, 1)));

        mvc.perform(post(TRANSACTIONS_PATH)
                        .content(jsonTransaction.write(actual).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = "classpath:integrationTest/createCategory.sql")
    void given_accountDoesNotExist_when_create_then_fail() throws Exception {
        TransactionDto actual = createTransactionDto(null)
                .setAccount(new AccountDto(999L, "name", LocalDate.of(2021, 1, 1)));

        mvc.perform(post(TRANSACTIONS_PATH)
                        .content(jsonTransaction.write(actual).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                    scripts = "classpath:integrationTest/createCategory.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                    scripts = "classpath:integrationTest/createAccount.sql"),
    })
    void given_entitiesToCreate_when_create_then_returnsEntitiesId() throws Exception {
        List<TransactionDto> toCreate = List.of(createTransactionDto(null));

        mvc.perform(post(TRANSACTIONS_PATH + "/bulk")
                        .content(jsonTransactionList.write(toCreate).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0]").isNumber());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = "classpath:integrationTest/createAll.sql")
    void given_entityExists_when_update_then_update() throws Exception {
        Long id = 1L;
        TransactionDto updated = createTransactionDto(id).setDescription("description updated");
        Transaction expected = createTransaction(id).setDescription("description updated");

        mvc.perform(put(TRANSACTIONS_PATH + "/" + id)
                        .content(jsonTransaction.write(updated).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        assertThat(repository.findById(id))
                .isPresent().contains(expected);
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                    scripts = "classpath:integrationTest/createCategory.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                    scripts = "classpath:integrationTest/createAccount.sql"),
    })
    void given_entityDoesNotExist_when_update_then_NotFound() throws Exception {
        TransactionDto updated = createTransactionDto(999L).setDescription("description updated");

        mvc.perform(put(TRANSACTIONS_PATH + "/" + 999L)
                        .content(jsonTransaction.write(updated).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        assertThat(repository.findById(999L)).isNotPresent();
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = "classpath:integrationTest/createAll.sql")
    void given_entityExists_when_deleted_then_doesNoLongerExist() throws Exception {
        Long id = 1L;
        mvc.perform(delete(TRANSACTIONS_PATH + "/" + id))
                .andExpect(status().is2xxSuccessful());

        assertThat(repository.findById(id)).isNotPresent();
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                    scripts = "classpath:integrationTest/createCategory.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                    scripts = "classpath:integrationTest/createAccount.sql"),
    })
    void given_stringToEvaluate_ifWellFormed_returnListOfDto() throws Exception {
        String headers = "Date,Price,Category,Account,Description";
        String toEvaluate = "31-12-2021,\"1,0 €\",name,name,description";
        List<TransactionDto> expected = List.of(createTransactionDto(null));

        mvc.perform(post(TRANSACTIONS_PATH + "/evaluate")
                .contentType(MediaType.TEXT_PLAIN)
                .param("headers", headers)
                .param("file", toEvaluate))
                .andExpect(content().json(jsonTransactionList.write(expected).getJson()));
    }

    private Transaction createTransaction(Long id) {
        return new Transaction(
                id,
                LocalDate.of(2021, 12, 31),
                1L,
                new Category(1L, "name", LocalDate.of(2021, 1, 1)),
                new Account(1L, "name", LocalDate.of(2021, 1, 1)),
                "description");
    }

    private TransactionDto createTransactionDto(Long id) {
        return new TransactionDto(
                id,
                LocalDate.of(2021, 12, 31),
                1L,
                new CategoryDto(1L, "name", LocalDate.of(2021, 1, 1)),
                new AccountDto(1L, "name", LocalDate.of(2021, 1, 1)),
                "description");
    }
}
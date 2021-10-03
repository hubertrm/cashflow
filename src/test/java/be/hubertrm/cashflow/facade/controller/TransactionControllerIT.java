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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SqlGroup({
    @Sql("classpath:integrationTest/createCategory.sql"),
    @Sql("classpath:integrationTest/createAccount.sql")
})
class TransactionControllerIT extends CashflowBaseIntegrationTest {

    public static final String TRANSACTIONS_PATH = API_PATH + "/transactions";

    @Autowired
    private JacksonTester<TransactionDto> jsonTransaction;
    @Autowired
    private JacksonTester<List<TransactionDto>> jsonTransactionList;
    @Autowired
    private TransactionRepository repository;

    @Test
    @Sql("classpath:integrationTest/deleteTransactions.sql")
    void given_noEntityExists_when_getAll_then_emptyList() throws Exception {
        mvc.perform(get(TRANSACTIONS_PATH))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonTransactionList.write(Collections.emptyList()).getJson()));
    }

    @Test
    @Sql("classpath:integrationTest/createTransaction.sql")
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
    void given_entityExists_when_getById_then_returnsEntity() throws Exception {
        Transaction current = createTransaction(null);
        Long id = repository.save(current);
        TransactionDto expected = createTransactionDto(id);

        mvc.perform(get(TRANSACTIONS_PATH + "/" + id))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonTransaction.write(expected).getJson()));
    }

    @Test
    @Sql("classpath:integrationTest/deleteTransactions.sql")
    void given_entityDoesNotExist_when_getById_then_NotFound() throws Exception {
        mvc.perform(get(TRANSACTIONS_PATH + "/" + 1L))
                .andExpect(status().is4xxClientError());
    }

    @Test
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
    void given_categoryDoesNotExist_when_create_then_fail() throws Exception {
        TransactionDto actual = createTransactionDto(null)
                .setCategory(new CategoryDto(999L, "name", LocalDate.of(2021, 1, 1)));

        mvc.perform(post(TRANSACTIONS_PATH)
                        .content(jsonTransaction.write(actual).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void given_accountDoesNotExist_when_create_then_fail() throws Exception {
        TransactionDto actual = createTransactionDto(null)
                .setAccount(new AccountDto(999L, "name", LocalDate.of(2021, 1, 1)));

        mvc.perform(post(TRANSACTIONS_PATH)
                        .content(jsonTransaction.write(actual).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
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
    void given_entityExists_when_update_then_update() throws Exception {
        Transaction current = createTransaction(null);
        Long id = repository.save(current);
        TransactionDto updated = createTransactionDto(id).setDescription("description updated");
        Transaction expected = current.setId(id).setDescription("description updated");

        mvc.perform(put(TRANSACTIONS_PATH + "/" + id)
                        .content(jsonTransaction.write(updated).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        assertThat(repository.findById(id))
                .isPresent().contains(expected);
    }

    @Test
    @Sql("classpath:integrationTest/deleteTransactions.sql")
    void given_entityDoesNotExist_when_update_then_NotFound() throws Exception {
        TransactionDto updated = createTransactionDto(1L).setDescription("description updated");

        mvc.perform(put(TRANSACTIONS_PATH + "/" + 1L)
                        .content(jsonTransaction.write(updated).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        assertThat(repository.findById(1L)).isNotPresent();
    }

    @Test
    void given_entityExists_when_deleted_then_doesNoLongerExist() throws Exception {
        Transaction current = createTransaction(null);
        Long id = repository.save(current);
        mvc.perform(delete(TRANSACTIONS_PATH + "/" + id))
                .andExpect(status().is2xxSuccessful());

        assertThat(repository.findById(id)).isNotPresent();
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
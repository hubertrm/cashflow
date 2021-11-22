package be.hubertrm.cashflow.domain.file.service;

import be.hubertrm.cashflow.domain.core.exception.ResourceNotFoundException;
import be.hubertrm.cashflow.domain.core.model.Account;
import be.hubertrm.cashflow.domain.core.model.Category;
import be.hubertrm.cashflow.domain.core.service.AccountService;
import be.hubertrm.cashflow.domain.core.service.CategoryService;
import be.hubertrm.cashflow.domain.file.model.Evaluation;
import be.hubertrm.cashflow.domain.file.model.RecordEvaluated;
import be.hubertrm.cashflow.domain.file.service.impl.EvaluatorServiceImpl;
import be.hubertrm.cashflow.facade.dto.TransactionDto;
import be.hubertrm.cashflow.sample.SampleDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class EvaluatorServiceTest {

    @MockBean
    private CategoryService categoryService;
    @MockBean
    private AccountService accountService;

    @InjectMocks
    @Autowired
    private EvaluatorServiceImpl evaluatorService;

    @BeforeEach
    void setup() throws ResourceNotFoundException {
        Category category = SampleDataService.createCategory();
        Account account = SampleDataService.createAccount();
        given(categoryService.getByName("test")).willReturn(category);
        given(accountService.getByName("test")).willReturn(account);
    }

    @Test
    void shouldUseDefaultLocale() {
        String[] fields = {"date", "price", "category", "account", "description"};
        String testLine = "01-09-2021,\"1,0 €\",test,test,test";
        RecordEvaluated expected = fromTransactionDto(SampleDataService.createTransactionDto()
                .setId(null)
                .setDate(LocalDate.of(2021, 9, 1)));

        RecordEvaluated actual = evaluatorService.create(fields, testLine);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void shouldRespectSelectedLocale() {
        String[] fieldsFr = {"date", "montant", "catégorie", "compte", "commentaire"};
        String testLine = "01-09-2021,\"1,0 €\",test,test,test";
        RecordEvaluated expected = fromTransactionDto(SampleDataService.createTransactionDto()
                .setId(null)
                .setDate(LocalDate.of(2021, 9, 1)));

        RecordEvaluated actual = evaluatorService.create(fieldsFr, testLine, Locale.FRANCE);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void shouldMatchFieldsOrder() {
        String[] fields = {"description", "account", "category", "price", "date"};
        String testLine = "test,test,test,\"1,0 €\",01-09-2021";
        RecordEvaluated expected = fromTransactionDto(SampleDataService.createTransactionDto()
                .setId(null)
                .setDate(LocalDate.of(2021, 9, 1)));

        RecordEvaluated actual = evaluatorService.create(fields, testLine);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void shouldIgnoreUnsupportedFields() {
        String[] fields = {"date", "price", "unsupported_1", "category", "account", "description", "unsupported_2"};
        String testLine = "01-09-2021,\"1,0 €\",unsupported_1,test,test,test,unsupported_2";
        RecordEvaluated expected = fromTransactionDto(SampleDataService.createTransactionDto()
                .setId(null)
                .setDate(LocalDate.of(2021, 9, 1)));

        RecordEvaluated actual = evaluatorService.create(fields, testLine);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void whenSelectedLocaleAndFieldsNameAreNotSupported_shouldReturnEmpty() {
        String[] fieldsIt = {"data", "importo", "categoria", "conto", "commento"};
        String testLine = "test,test,test,test,test";
        RecordEvaluated expected = new RecordEvaluated();

        RecordEvaluated actual = evaluatorService.create(fieldsIt, testLine, Locale.ITALY);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    private RecordEvaluated fromTransactionDto(TransactionDto transactionDto) {
        return new RecordEvaluated()
                .setDate(new Evaluation().setValue(transactionDto.getDate().toString()))
                .setAmount(new Evaluation().setValue(String.valueOf(transactionDto.getAmount())))
                .setCategory(new Evaluation().setValue(String.valueOf(transactionDto.getCategory().getName())))
                .setAccount(new Evaluation().setValue(String.valueOf(transactionDto.getAccount().getName())))
                .setDescription(new Evaluation().setValue(String.valueOf(transactionDto.getDescription())));
    }
}
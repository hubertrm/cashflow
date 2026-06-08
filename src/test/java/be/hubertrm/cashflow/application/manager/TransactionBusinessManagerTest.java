package be.hubertrm.cashflow.application.manager;

import be.hubertrm.cashflow.application.dto.AccountDto;
import be.hubertrm.cashflow.application.dto.CategoryDto;
import be.hubertrm.cashflow.application.dto.RecordEvaluatedDto;
import be.hubertrm.cashflow.application.dto.TransactionDto;
import be.hubertrm.cashflow.domain.core.exception.ResourceNotFoundException;
import be.hubertrm.cashflow.domain.core.model.Account;
import be.hubertrm.cashflow.domain.core.model.Category;
import be.hubertrm.cashflow.domain.core.model.Transaction;
import be.hubertrm.cashflow.domain.core.service.AccountService;
import be.hubertrm.cashflow.domain.core.service.CategoryService;
import be.hubertrm.cashflow.domain.core.service.TransactionService;
import be.hubertrm.cashflow.domain.file.model.RecordEvaluated;
import be.hubertrm.cashflow.domain.file.service.reader.CsvReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
class TransactionBusinessManagerTest {

    @Autowired
    private TransactionBusinessManager manager;

    @MockitoBean
    private TransactionService transactionService;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private CsvReader csvReader;

    private static final LocalDate NOW = LocalDate.of(2021, 12, 31);
    private static final CategoryDto CATEGORY_DTO = new CategoryDto(1L, "Food", NOW);
    private static final AccountDto ACCOUNT_DTO = new AccountDto(1L, "Checking", NOW);

    @Nested
    class getAllDesigns {

        @Test
        @DisplayName("getAll returns list of TransactionDto")
        void testGetAll() {
            Transaction transaction = new Transaction(1L, NOW, 100f,
                    new Category(1L, "Food", NOW),
                    new Account(1L, "Checking", NOW),
                    "groceries");
            when(transactionService.getAll()).thenReturn(List.of(transaction));

            List<TransactionDto> result = manager.getAll();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getDescription()).isEqualTo("groceries");
            verify(transactionService).getAll();
        }

        @Test
        @DisplayName("getAll paginated returns Page of TransactionDto")
        void testGetAllPaginated() {
            Transaction transaction = new Transaction(1L, NOW, 100f,
                    new Category(1L, "Food", NOW),
                    new Account(1L, "Checking", NOW),
                    "groceries");
            PageRequest pageable = PageRequest.of(0, 10);
            Page<Transaction> page = new PageImpl<>(List.of(transaction), pageable, 1);
            when(transactionService.getAll(pageable)).thenReturn(page);

            Page<TransactionDto> result = manager.getAll(pageable);

            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0).getDescription()).isEqualTo("groceries");
        }
    }

    @Nested
    class getByIdDesigns {

        @Test
        @DisplayName("getById returns TransactionDto when found")
        void testGetById() throws ResourceNotFoundException {
            Transaction transaction = new Transaction(1L, NOW, 100f,
                    new Category(1L, "Food", NOW),
                    new Account(1L, "Checking", NOW),
                    "groceries");
            when(transactionService.getById(1L)).thenReturn(transaction);

            TransactionDto result = manager.getById(1L);

            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getDescription()).isEqualTo("groceries");
        }

        @Test
        @DisplayName("getById throws ResourceNotFoundException when not found")
        void testGetByIdThrows() throws ResourceNotFoundException {
            when(transactionService.getById(99L)).thenThrow(new ResourceNotFoundException("Transaction not found", 99L));

            assertThatThrownBy(() -> manager.getById(99L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    class createDesigns {

        @Test
        @DisplayName("create succeeds when category and account exist")
        void testCreate() throws ResourceNotFoundException {
            TransactionDto dto = new TransactionDto(null, NOW, 100f, CATEGORY_DTO, ACCOUNT_DTO, "groceries");
            when(categoryService.exists(1L)).thenReturn(true);
            when(accountService.exists(1L)).thenReturn(true);
            when(transactionService.create(any(Transaction.class))).thenReturn(1L);

            Long result = manager.create(dto);

            assertThat(result).isEqualTo(1L);
            verify(categoryService).exists(1L);
            verify(accountService).exists(1L);
        }

        @Test
        @DisplayName("create throws when category does not exist")
        void testCreateThrowsWhenCategoryMissing() {
            TransactionDto dto = new TransactionDto(null, NOW, 100f, CATEGORY_DTO, ACCOUNT_DTO, "groceries");
            when(categoryService.exists(1L)).thenReturn(false);

            assertThatThrownBy(() -> manager.create(dto))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Category");
        }

        @Test
        @DisplayName("create throws when category id is null")
        void testCreateThrowsWhenCategoryIdNull() {
            TransactionDto dto = new TransactionDto(null, NOW, 100f,
                    new CategoryDto(null, "Food", NOW), ACCOUNT_DTO, "groceries");

            assertThatThrownBy(() -> manager.create(dto))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Category");
        }

        @Test
        @DisplayName("create throws when account does not exist")
        void testCreateThrowsWhenAccountMissing() {
            TransactionDto dto = new TransactionDto(null, NOW, 100f, CATEGORY_DTO, ACCOUNT_DTO, "groceries");
            when(categoryService.exists(1L)).thenReturn(true);
            when(accountService.exists(1L)).thenReturn(false);

            assertThatThrownBy(() -> manager.create(dto))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Account");
        }
    }

    @Nested
    class createBulkDesigns {

        @Test
        @DisplayName("create bulk succeeds when all categories and accounts exist")
        void testCreateBulk() throws ResourceNotFoundException {
            TransactionDto dto = new TransactionDto(null, NOW, 100f, CATEGORY_DTO, ACCOUNT_DTO, "groceries");
            when(categoryService.exists(1L)).thenReturn(true);
            when(accountService.exists(1L)).thenReturn(true);
            when(transactionService.createAll(anyList())).thenReturn(List.of(1L));

            List<Long> result = manager.create(List.of(dto));

            assertThat(result).containsExactly(1L);
        }

        @Test
        @DisplayName("create bulk throws when one category does not exist")
        void testCreateBulkThrowsWhenCategoryMissing() {
            TransactionDto dto = new TransactionDto(null, NOW, 100f, CATEGORY_DTO, ACCOUNT_DTO, "groceries");
            when(categoryService.exists(1L)).thenReturn(false);

            assertThatThrownBy(() -> manager.create(List.of(dto)))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Category");
        }
    }

    @Nested
    class updateDesigns {

        @Test
        @DisplayName("update succeeds when category and account exist")
        void testUpdate() throws ResourceNotFoundException {
            TransactionDto dto = new TransactionDto(1L, NOW, 200f, CATEGORY_DTO, ACCOUNT_DTO, "updated");
            when(categoryService.exists(1L)).thenReturn(true);
            when(accountService.exists(1L)).thenReturn(true);
            doNothing().when(transactionService).update(eq(1L), any(Transaction.class));

            assertThatNoException().isThrownBy(() -> manager.update(1L, dto));
            verify(transactionService).update(eq(1L), any(Transaction.class));
        }
    }

    @Nested
    class deleteDesigns {

        @Test
        @DisplayName("deleteById delegates to service")
        void testDeleteById() throws ResourceNotFoundException {
            doNothing().when(transactionService).deleteById(1L);

            assertThatNoException().isThrownBy(() -> manager.deleteById(1L));
            verify(transactionService).deleteById(1L);
        }
    }

    @Nested
    class evaluateFileDesigns {

        @Test
        @DisplayName("evaluateFileWithHeaders delegates to csvReader")
        void testEvaluateFileWithHeaders() {
            RecordEvaluated record = new RecordEvaluated();
            when(csvReader.read("date,amount", "2021-01-01,100", Locale.ROOT))
                    .thenReturn(List.of(record));

            List<RecordEvaluatedDto> result = manager.evaluateFileWithHeaders("date,amount", "2021-01-01,100");

            assertThat(result).hasSize(1);
            verify(csvReader).read("date,amount", "2021-01-01,100", Locale.ROOT);
        }
    }
}
package be.hubertrm.cashflow.application.manager;

import be.hubertrm.cashflow.application.dto.SyncRequestDto;
import be.hubertrm.cashflow.application.dto.SyncResultDto;
import be.hubertrm.cashflow.domain.core.model.Account;
import be.hubertrm.cashflow.domain.core.model.Category;
import be.hubertrm.cashflow.domain.core.model.Transaction;
import be.hubertrm.cashflow.domain.core.service.AccountService;
import be.hubertrm.cashflow.domain.core.service.CategoryService;
import be.hubertrm.cashflow.domain.core.service.TransactionService;
import be.hubertrm.cashflow.domain.sheets.entity.RangeOptions;
import be.hubertrm.cashflow.domain.sheets.service.SheetsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SyncBusinessManagerTest {

    @Autowired
    private SyncBusinessManager manager;

    @MockitoBean
    private SheetsService sheetsService;

    @MockitoBean
    private TransactionService transactionService;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private AccountService accountService;

    @Nested
    class syncSheetToDatabaseDesigns {

        @Test
        @DisplayName("Returns empty result when sheet data is null")
        void testSyncWithNullData() {
            SyncRequestDto request = createRequest();
            when(sheetsService.getRangeBySpreadSheetIdRangeAndValueRenderOption(
                    eq("sheet1"), eq("A:Z"), any(RangeOptions.class)))
                    .thenReturn(null);

            SyncResultDto result = manager.syncSheetToDatabase(request);

            assertThat(result.getTotalRows()).isZero();
            assertThat(result.getSuccessCount()).isZero();
        }

        @Test
        @DisplayName("Returns empty result when sheet data is empty")
        void testSyncWithEmptyData() {
            SyncRequestDto request = createRequest();
            when(sheetsService.getRangeBySpreadSheetIdRangeAndValueRenderOption(
                    eq("sheet1"), eq("A:Z"), any(RangeOptions.class)))
                    .thenReturn(new ArrayList<>());

            SyncResultDto result = manager.syncSheetToDatabase(request);

            assertThat(result.getTotalRows()).isZero();
            assertThat(result.getSuccessCount()).isZero();
        }

        @Test
        @DisplayName("Successfully syncs a single row from sheets")
        void testSyncSingleRow() {
            SyncRequestDto request = createRequest();
            List<List<Object>> sheetData = List.of(
                    List.of("2021-12-31", "100.50", "Food", "Checking", "groceries")
            );
            when(sheetsService.getRangeBySpreadSheetIdRangeAndValueRenderOption(
                    eq("sheet1"), eq("A:Z"), any(RangeOptions.class)))
                    .thenReturn(sheetData);

            Category category = new Category(1L, "Food", LocalDate.now());
            Account account = new Account(1L, "Checking", LocalDate.now());
            when(categoryService.findByName("Food")).thenReturn(Optional.of(category));
            when(accountService.findByName("Checking")).thenReturn(Optional.of(account));
            when(transactionService.findDuplicate(any(), any(), anyLong(), anyLong(), any(), any()))
                    .thenReturn(Optional.empty());
            when(transactionService.create(any(Transaction.class))).thenReturn(1L);

            SyncResultDto result = manager.syncSheetToDatabase(request);

            assertThat(result.getTotalRows()).isEqualTo(1);
            assertThat(result.getSuccessCount()).isEqualTo(1);
            assertThat(result.getSkippedDuplicates()).isZero();
            assertThat(result.getErrors()).isEmpty();
        }

        @Test
        @DisplayName("Skips duplicate transactions")
        void testSkipsDuplicates() {
            SyncRequestDto request = createRequest();
            List<List<Object>> sheetData = List.of(
                    List.of("2021-12-31", "100.50", "Food", "Checking", "groceries")
            );
            when(sheetsService.getRangeBySpreadSheetIdRangeAndValueRenderOption(
                    eq("sheet1"), eq("A:Z"), any(RangeOptions.class)))
                    .thenReturn(sheetData);

            Transaction existingTx = new Transaction();
            when(categoryService.findByName("Food"))
                    .thenReturn(Optional.of(new Category(1L, "Food", LocalDate.now())));
            when(accountService.findByName("Checking"))
                    .thenReturn(Optional.of(new Account(1L, "Checking", LocalDate.now())));
            when(transactionService.findDuplicate(any(), any(), anyLong(), anyLong(), any(), any()))
                    .thenReturn(Optional.of(existingTx));

            SyncResultDto result = manager.syncSheetToDatabase(request);

            assertThat(result.getSuccessCount()).isZero();
            assertThat(result.getSkippedDuplicates()).isEqualTo(1);
        }

        @Test
        @DisplayName("Creates new categories when they don't exist")
        void testCreatesNewCategory() {
            SyncRequestDto request = createRequest();
            List<List<Object>> sheetData = List.of(
                    List.of("2021-12-31", "100.50", "NewCategory", "Checking", "desc")
            );
            when(sheetsService.getRangeBySpreadSheetIdRangeAndValueRenderOption(
                    eq("sheet1"), eq("A:Z"), any(RangeOptions.class)))
                    .thenReturn(sheetData);

            when(categoryService.findByName("NewCategory")).thenReturn(Optional.empty());
            when(categoryService.create(any(Category.class))).thenReturn(99L);
            when(accountService.findByName("Checking"))
                    .thenReturn(Optional.of(new Account(1L, "Checking", LocalDate.now())));
            when(transactionService.findDuplicate(any(), any(), anyLong(), anyLong(), any(), any()))
                    .thenReturn(Optional.empty());
            when(transactionService.create(any(Transaction.class))).thenReturn(1L);

            SyncResultDto result = manager.syncSheetToDatabase(request);

            assertThat(result.getNewCategoriesCreated()).isEqualTo(1);
            assertThat(result.getSuccessCount()).isEqualTo(1);
            verify(categoryService).create(any(Category.class));
        }

        @Test
        @DisplayName("Creates new accounts when they don't exist")
        void testCreatesNewAccount() {
            SyncRequestDto request = createRequest();
            List<List<Object>> sheetData = List.of(
                    List.of("2021-12-31", "100.50", "Food", "NewAccount", "desc")
            );
            when(sheetsService.getRangeBySpreadSheetIdRangeAndValueRenderOption(
                    eq("sheet1"), eq("A:Z"), any(RangeOptions.class)))
                    .thenReturn(sheetData);

            when(categoryService.findByName("Food"))
                    .thenReturn(Optional.of(new Category(1L, "Food", LocalDate.now())));
            when(accountService.findByName("NewAccount")).thenReturn(Optional.empty());
            when(accountService.create(any(Account.class))).thenReturn(99L);
            when(transactionService.findDuplicate(any(), any(), anyLong(), anyLong(), any(), any()))
                    .thenReturn(Optional.empty());
            when(transactionService.create(any(Transaction.class))).thenReturn(1L);

            SyncResultDto result = manager.syncSheetToDatabase(request);

            assertThat(result.getNewAccountsCreated()).isEqualTo(1);
            assertThat(result.getSuccessCount()).isEqualTo(1);
            verify(accountService).create(any(Account.class));
        }

        @Test
        @DisplayName("Caches categories and accounts - second row reuses cached")
        void testCachingCategoryAndAccount() {
            SyncRequestDto request = createRequest();
            List<List<Object>> sheetData = List.of(
                    List.of("2021-12-31", "100.50", "Food", "Checking", "first"),
                    List.of("2021-12-30", "200.00", "Food", "Checking", "second")
            );
            when(sheetsService.getRangeBySpreadSheetIdRangeAndValueRenderOption(
                    eq("sheet1"), eq("A:Z"), any(RangeOptions.class)))
                    .thenReturn(sheetData);

            when(categoryService.findByName("Food"))
                    .thenReturn(Optional.of(new Category(1L, "Food", LocalDate.now())));
            when(accountService.findByName("Checking"))
                    .thenReturn(Optional.of(new Account(1L, "Checking", LocalDate.now())));
            when(transactionService.findDuplicate(any(), any(), anyLong(), anyLong(), any(), any()))
                    .thenReturn(Optional.empty());
            when(transactionService.create(any(Transaction.class))).thenReturn(1L, 2L);

            SyncResultDto result = manager.syncSheetToDatabase(request);

            // Should only call findByName once — second row hits cache
            verify(categoryService, times(1)).findByName("Food");
            verify(accountService, times(1)).findByName("Checking");
            assertThat(result.getSuccessCount()).isEqualTo(2);
            assertThat(result.getNewCategoriesCreated()).isZero();
        }

        @Test
        @DisplayName("Records errors for rows that fail to parse")
        void testRecordsRowErrors() {
            SyncRequestDto request = new SyncRequestDto();
            request.setSheetId("sheet1");
            request.setRange("A:Z");
            List<SyncRequestDto.HeaderMapping> headers = new ArrayList<>();
            SyncRequestDto.HeaderMapping amountHeader = new SyncRequestDto.HeaderMapping();
            amountHeader.setHeader("amount");
            amountHeader.setHeaderType("number");
            headers.add(amountHeader);
            request.setHeaders(headers);

            List<List<Object>> sheetData = List.of(
                    List.of("not-a-number")
            );
            when(sheetsService.getRangeBySpreadSheetIdRangeAndValueRenderOption(
                    eq("sheet1"), eq("A:Z"), any(RangeOptions.class)))
                    .thenReturn(sheetData);

            SyncResultDto result = manager.syncSheetToDatabase(request);

            assertThat(result.getTotalRows()).isEqualTo(1);
            assertThat(result.getErrors()).hasSize(1);
            assertThat(result.getErrors().get(0).getRowIndex()).isZero();
        }

        @Test
        @DisplayName("Handles Excel serial dates via Number type")
        void testExcelSerialDate() {
            SyncRequestDto request = new SyncRequestDto();
            request.setSheetId("sheet1");
            request.setRange("A:Z");
            List<SyncRequestDto.HeaderMapping> headers = List.of(
                    createHeader("date", "date"),
                    createHeader("amount", "number"),
                    createHeader("category", "string"),
                    createHeader("account", "string"),
                    createHeader("description", "string")
            );
            request.setHeaders(headers);

            // Excel serial date: 1 = 1899-12-31, so 44561 = 2021-12-31
            double excelDate = 44561.0;
            List<List<Object>> sheetData = List.of(
                    List.of(excelDate, "100.50", "Food", "Checking", "excel date test")
            );
            when(sheetsService.getRangeBySpreadSheetIdRangeAndValueRenderOption(
                    eq("sheet1"), eq("A:Z"), any(RangeOptions.class)))
                    .thenReturn(sheetData);

            when(categoryService.findByName("Food"))
                    .thenReturn(Optional.of(new Category(1L, "Food", LocalDate.now())));
            when(accountService.findByName("Checking"))
                    .thenReturn(Optional.of(new Account(1L, "Checking", LocalDate.now())));
            when(transactionService.findDuplicate(any(), any(), anyLong(), anyLong(), any(), any()))
                    .thenReturn(Optional.empty());
            when(transactionService.create(any(Transaction.class))).thenReturn(1L);

            SyncResultDto result = manager.syncSheetToDatabase(request);

            assertThat(result.getSuccessCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("Handles checkbox headerType for boolean parsing")
        void testCheckboxHeaderType() {
            SyncRequestDto request = new SyncRequestDto();
            request.setSheetId("sheet1");
            request.setRange("A:Z");
            List<SyncRequestDto.HeaderMapping> headers = new ArrayList<>();
            headers.add(createHeader("date", "string"));
            headers.add(createHeader("amount", "number"));
            headers.add(createHeader("category", "string"));
            headers.add(createHeader("account", "string"));
            headers.add(createHeader("description", "string"));
            headers.add(createHeader("commun", "checkbox"));
            request.setHeaders(headers);

            List<List<Object>> sheetData = List.of(
                    List.of("2021-12-31", "100.50", "Food", "Checking", "desc", true)
            );
            when(sheetsService.getRangeBySpreadSheetIdRangeAndValueRenderOption(
                    eq("sheet1"), eq("A:Z"), any(RangeOptions.class)))
                    .thenReturn(sheetData);

            when(categoryService.findByName("Food"))
                    .thenReturn(Optional.of(new Category(1L, "Food", LocalDate.now())));
            when(accountService.findByName("Checking"))
                    .thenReturn(Optional.of(new Account(1L, "Checking", LocalDate.now())));
            when(transactionService.findDuplicate(any(), any(), anyLong(), anyLong(), any(), any()))
                    .thenReturn(Optional.empty());
            when(transactionService.create(any(Transaction.class))).thenReturn(1L);

            SyncResultDto result = manager.syncSheetToDatabase(request);

            assertThat(result.getSuccessCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("Handles null cell values gracefully")
        void testNullCellValues() {
            SyncRequestDto request = createRequest();
            List<List<Object>> sheetData = new ArrayList<>();
            List<Object> row = new ArrayList<>();
            row.add("2021-12-31");
            row.add("100.50");
            row.add("Food");
            row.add(null);  // null account — should skip row
            row.add("desc");
            sheetData.add(row);
            when(sheetsService.getRangeBySpreadSheetIdRangeAndValueRenderOption(
                    eq("sheet1"), eq("A:Z"), any(RangeOptions.class)))
                    .thenReturn(sheetData);

            when(categoryService.findByName("Food"))
                    .thenReturn(Optional.of(new Category(1L, "Food", LocalDate.now())));

            SyncResultDto result = manager.syncSheetToDatabase(request);

            assertThat(result.getSuccessCount()).isZero();
        }

        @Test
        @DisplayName("Handles numeric values for float and integer parsing")
        void testNumericValueParsing() {
            SyncRequestDto request = new SyncRequestDto();
            request.setSheetId("sheet1");
            request.setRange("A:Z");
            List<SyncRequestDto.HeaderMapping> headers = new ArrayList<>();
            headers.add(createHeader("date", "string"));
            headers.add(createHeader("amount", "number"));
            headers.add(createHeader("category", "string"));
            headers.add(createHeader("account", "string"));
            headers.add(createHeader("description", "string"));
            headers.add(createHeader("nbrofactions", "number"));
            headers.add(createHeader("change", "number"));
            headers.add(createHeader("beforeconversion", "number"));
            headers.add(createHeader("reference", "number"));
            request.setHeaders(headers);

            List<List<Object>> sheetData = List.of(
                    List.of("2021-12-31", 100.50, "Food", "Checking", "desc", 10, 5.5, 1.25, 12345L)
            );
            when(sheetsService.getRangeBySpreadSheetIdRangeAndValueRenderOption(
                    eq("sheet1"), eq("A:Z"), any(RangeOptions.class)))
                    .thenReturn(sheetData);

            when(categoryService.findByName("Food"))
                    .thenReturn(Optional.of(new Category(1L, "Food", LocalDate.now())));
            when(accountService.findByName("Checking"))
                    .thenReturn(Optional.of(new Account(1L, "Checking", LocalDate.now())));
            when(transactionService.findDuplicate(any(), any(), anyLong(), anyLong(), any(), any()))
                    .thenReturn(Optional.empty());
            when(transactionService.create(any(Transaction.class))).thenReturn(1L);

            SyncResultDto result = manager.syncSheetToDatabase(request);

            assertThat(result.getSuccessCount()).isEqualTo(1);
        }
    }

    private SyncRequestDto createRequest() {
        SyncRequestDto request = new SyncRequestDto();
        request.setSheetId("sheet1");
        request.setRange("A:Z");
        List<SyncRequestDto.HeaderMapping> headers = new ArrayList<>();
        headers.add(createHeader("date", "string"));
        headers.add(createHeader("amount", "number"));
        headers.add(createHeader("category", "string"));
        headers.add(createHeader("account", "string"));
        headers.add(createHeader("description", "string"));
        request.setHeaders(headers);
        return request;
    }

    private SyncRequestDto.HeaderMapping createHeader(String header, String headerType) {
        SyncRequestDto.HeaderMapping hm = new SyncRequestDto.HeaderMapping();
        hm.setHeader(header);
        hm.setHeaderType(headerType);
        return hm;
    }
}
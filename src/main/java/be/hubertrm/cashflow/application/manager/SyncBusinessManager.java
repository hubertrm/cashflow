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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class SyncBusinessManager {

    @Resource
    private SheetsService sheetsService;
    @Resource
    private TransactionService transactionService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private AccountService accountService;

    private static final RangeOptions DEFAULT_RANGE_OPTIONS = new RangeOptions("ROWS", "UNFORMATTED_VALUE");

    @Transactional
    public SyncResultDto syncSheetToDatabase(SyncRequestDto request) {
        SyncResultDto result = new SyncResultDto();
        Map<String, Category> categoryCache = new HashMap<>();
        Map<String, Account> accountCache = new HashMap<>();

        List<List<Object>> sheetData = sheetsService.getRangeBySpreadSheetIdRangeAndValueRenderOption(
                request.getSheetId(), request.getRange(), DEFAULT_RANGE_OPTIONS);

        if (sheetData == null || sheetData.isEmpty()) {
            log.warn("No data returned from sheet {} with range {}", request.getSheetId(), request.getRange());
            return result;
        }

        result.setTotalRows(sheetData.size());
        List<SyncRequestDto.HeaderMapping> headers = request.getHeaders();

        for (int rowIndex = 0; rowIndex < sheetData.size(); rowIndex++) {
            List<Object> row = sheetData.get(rowIndex);
            try {
                Transaction transaction = parseRowToTransaction(row, headers, categoryCache, accountCache, result);
                if (transaction == null) {
                    continue;
                }

                Optional<Transaction> duplicate = transactionService.findDuplicate(
                        transaction.getDate(),
                        transaction.getAmount(),
                        transaction.getCategory().getId(),
                        transaction.getAccount().getId(),
                        transaction.getDescription(),
                        transaction.getReference()
                );

                if (duplicate.isPresent()) {
                    result.setSkippedDuplicates(result.getSkippedDuplicates() + 1);
                    log.debug("Skipping duplicate transaction at row {}", rowIndex);
                } else {
                    transactionService.create(transaction);
                    result.setSuccessCount(result.getSuccessCount() + 1);
                }
            } catch (Exception e) {
                log.error("Error processing row {}: {}", rowIndex, e.getMessage(), e);
                result.getErrors().add(new SyncResultDto.SyncErrorDto(rowIndex, e.getMessage()));
            }
        }

        return result;
    }

    private Transaction parseRowToTransaction(
            List<Object> row,
            List<SyncRequestDto.HeaderMapping> headers,
            Map<String, Category> categoryCache,
            Map<String, Account> accountCache,
            SyncResultDto result
    ) {
        Transaction transaction = new Transaction();
        String categoryName = null;
        String accountName = null;

        for (int colIndex = 0; colIndex < headers.size() && colIndex < row.size(); colIndex++) {
            SyncRequestDto.HeaderMapping header = headers.get(colIndex);
            Object cellValue = row.get(colIndex);

            if (cellValue == null || cellValue.toString().isEmpty()) {
                continue;
            }

            String headerName = header.getHeader().toLowerCase();
            String headerType = header.getHeaderType();

            switch (headerName) {
                case "date":
                    transaction.setDate(parseDate(cellValue, headerType));
                    break;
                case "amount":
                    transaction.setAmount(parseFloat(cellValue));
                    break;
                case "category":
                    categoryName = cellValue.toString().trim();
                    break;
                case "account":
                    accountName = cellValue.toString().trim();
                    break;
                case "comment":
                case "description":
                    transaction.setDescription(cellValue.toString());
                    break;
                case "holiday":
                    transaction.setHoliday(cellValue.toString());
                    break;
                case "month":
                    transaction.setMonth(cellValue.toString());
                    break;
                case "ticker":
                    transaction.setTicker(cellValue.toString());
                    break;
                case "nbrofactions":
                    transaction.setNbrOfActions(parseInteger(cellValue));
                    break;
                case "change":
                    transaction.setChangeRate(parseFloat(cellValue));
                    break;
                case "commun":
                    transaction.setIsCommon(parseBoolean(cellValue, headerType));
                    break;
                case "beforeconversion":
                    transaction.setBeforeConversion(parseFloat(cellValue));
                    break;
                case "currency":
                    transaction.setCurrency(cellValue.toString());
                    break;
                case "reference":
                    transaction.setReference(parseLong(cellValue));
                    break;
                default:
                    log.trace("Unknown header: {}", headerName);
            }
        }

        if (categoryName == null || accountName == null) {
            log.warn("Skipping row: missing category or account");
            return null;
        }

        Category category = getOrCreateCategory(categoryName, categoryCache, result);
        Account account = getOrCreateAccount(accountName, accountCache, result);

        transaction.setCategory(category);
        transaction.setAccount(account);

        return transaction;
    }

    private LocalDate parseDate(Object value, String headerType) {
        if ("date".equals(headerType) && value instanceof Number) {
            // Excel serial date to LocalDate
            long excelSerialDate = ((Number) value).longValue();
            return LocalDate.of(1899, 12, 30).plusDays(excelSerialDate);
        }
        return LocalDate.parse(value.toString());
    }

    private Float parseFloat(Object value) {
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        return Float.parseFloat(value.toString());
    }

    private Integer parseInteger(Object value) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Long parseLong(Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Boolean parseBoolean(Object value, String headerType) {
        if ("checkbox".equals(headerType)) {
            if (value instanceof Boolean) {
                return (Boolean) value;
            }
            String strValue = value.toString().toLowerCase();
            return "true".equals(strValue) || "1".equals(strValue) || "yes".equals(strValue);
        }
        return Boolean.parseBoolean(value.toString());
    }

    private Category getOrCreateCategory(String name, Map<String, Category> cache, SyncResultDto result) {
        if (cache.containsKey(name)) {
            return cache.get(name);
        }

        Optional<Category> existing = categoryService.findByName(name);
        if (existing.isPresent()) {
            cache.put(name, existing.get());
            return existing.get();
        }

        Category newCategory = new Category(null, name, LocalDate.now());
        Long id = categoryService.create(newCategory);
        newCategory.setId(id);
        cache.put(name, newCategory);
        result.setNewCategoriesCreated(result.getNewCategoriesCreated() + 1);
        log.info("Created new category: {}", name);
        return newCategory;
    }

    private Account getOrCreateAccount(String name, Map<String, Account> cache, SyncResultDto result) {
        if (cache.containsKey(name)) {
            return cache.get(name);
        }

        Optional<Account> existing = accountService.findByName(name);
        if (existing.isPresent()) {
            cache.put(name, existing.get());
            return existing.get();
        }

        Account newAccount = new Account(null, name, LocalDate.now());
        Long id = accountService.create(newAccount);
        newAccount.setId(id);
        cache.put(name, newAccount);
        result.setNewAccountsCreated(result.getNewAccountsCreated() + 1);
        log.info("Created new account: {}", name);
        return newAccount;
    }
}

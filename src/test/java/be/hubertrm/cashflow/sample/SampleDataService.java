package be.hubertrm.cashflow.sample;

import be.hubertrm.cashflow.domain.core.model.Account;
import be.hubertrm.cashflow.domain.core.model.Category;
import be.hubertrm.cashflow.domain.core.model.Transaction;
import be.hubertrm.cashflow.facade.dto.AccountDto;
import be.hubertrm.cashflow.facade.dto.CategoryDto;
import be.hubertrm.cashflow.facade.dto.TransactionDto;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class SampleDataService {

    private SampleDataService() {}

    public static Transaction createTransaction() {
        return createTransaction(1L, LocalDate.now(), 1F, createCategory(), createAccount(), "test");
    }

    public static  Transaction createTransaction(long id, LocalDate payDate, float amount, Category category,
                                                 Account account, String comments) {
        return new Transaction(id, payDate, amount, category, account, comments);
    }

    public static Category createCategory() {
        return createCategory(1L, "test", LocalDate.now());
    }

    public static Category createCategory(Long id, String name, LocalDate creationDate) {
        return new Category(id, name, creationDate);
    }

    public static Account createAccount() {
        return createAccount(1L, "test", LocalDate.now());
    }

    public static Account createAccount(Long id, String name, LocalDate creationDate) {
        return new Account(id, name, creationDate);
    }

    public static CategoryDto createCategoryDto() {
        return createCategoryDto(1L);
    }

    public static CategoryDto createCategoryDto(Long id) {
        return createCategoryDto(id, "test", LocalDate.now());
    }

    public static CategoryDto createCategoryDto(Long id, String name, LocalDate creationDate) {
        return new CategoryDto(id, name, creationDate);
    }

    public static TransactionDto createTransactionDto() {
        return createTransactionDto(1L, LocalDate.now(), 1F, createCategoryDto(), createAccountDto(), "test");
    }

    public static TransactionDto createTransactionDto(Long id, LocalDate payDate, Float amount, CategoryDto categoryDto, AccountDto accountDto, String comments) {
        return new TransactionDto(id, payDate, amount, categoryDto, accountDto, comments);
    }

    public static List<TransactionDto> createTransactionDtoList() {
        return Arrays.asList(createTransactionDto(), createTransactionDto());
    }

    public static AccountDto createAccountDto() {
        return createAccountDto(1L);
    }

    public static AccountDto createAccountDto(Long id) {
        return createAccountDto(id, "test", LocalDate.now());
    }

    public static AccountDto createAccountDto(Long id, String name, LocalDate creationDate) {
        return new AccountDto(id, name, creationDate);
    }
}

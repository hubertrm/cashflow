package be.hubertrm.cashflow.domain.core.service;

import be.hubertrm.cashflow.domain.core.model.Transaction;

import java.time.LocalDate;
import java.util.Optional;

public interface TransactionService extends Service<Transaction> {

    Optional<Transaction> findDuplicate(LocalDate date, Float amount, Long categoryId, Long accountId);
}

package be.hubertrm.cashflow.domain.core.repository;

import be.hubertrm.cashflow.domain.core.model.Transaction;

import java.time.LocalDate;
import java.util.Optional;

public interface TransactionRepository extends Repository<Transaction> {

    Optional<Transaction> findDuplicate(LocalDate date, Float amount, Long categoryId, Long accountId);
}

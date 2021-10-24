package be.hubertrm.cashflow.domain.core.repository;

import be.hubertrm.cashflow.domain.core.model.Transaction;

import java.util.List;

public interface TransactionRepository extends Repository<Transaction> {

    List<Long> save(List<Transaction> transactions);
}

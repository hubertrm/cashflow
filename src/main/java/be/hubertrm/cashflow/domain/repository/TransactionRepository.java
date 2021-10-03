package be.hubertrm.cashflow.domain.repository;

import be.hubertrm.cashflow.domain.model.Transaction;

import java.util.List;

public interface TransactionRepository extends Repository<Transaction> {

    List<Long> save(List<Transaction> transactions);
}

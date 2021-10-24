package be.hubertrm.cashflow.domain.core.service;

import be.hubertrm.cashflow.domain.core.model.Transaction;

import java.util.List;

public interface TransactionService extends Service<Transaction> {

    List<Long> create(List<Transaction> transactions);
}

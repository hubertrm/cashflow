package be.hubertrm.cashflow.domain.service;

import be.hubertrm.cashflow.domain.model.Transaction;

import java.util.List;

public interface TransactionService extends Service<Transaction> {

    List<Long> create(List<Transaction> transactions);
}

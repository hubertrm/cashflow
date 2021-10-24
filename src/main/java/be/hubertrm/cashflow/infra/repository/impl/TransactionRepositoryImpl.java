package be.hubertrm.cashflow.infra.repository.impl;

import be.hubertrm.cashflow.domain.core.model.Transaction;
import be.hubertrm.cashflow.domain.core.repository.TransactionRepository;
import be.hubertrm.cashflow.infra.entity.TransactionEntity;
import be.hubertrm.cashflow.infra.repository.JpaTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final JpaTransactionRepository repository;

    public TransactionRepositoryImpl(JpaTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Transaction> findById(Long transactionId) {
        return repository.findById(transactionId).map(TransactionEntity::fromThis);
    }

    @Override
    public List<Transaction> getAll() {
        return repository.findAll().stream().map(TransactionEntity::fromThis).collect(Collectors.toList());
    }

    @Override
    public Long save(Transaction transaction) {
        return repository.save(TransactionEntity.from(transaction)).getId();
    }

    @Override
    public List<Long> save(List<Transaction> transactions) {
        return repository.saveAll(transactions.stream()
                        .map(TransactionEntity::from)
                        .collect(Collectors.toList()))
                .stream().map(TransactionEntity::getId)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Long transactionId, Transaction transaction) {
        transaction.setId(transactionId);
        save(transaction);
    }

    @Override
    public void deleteById(Long transactionId) {
        repository.deleteById(transactionId);
    }
}

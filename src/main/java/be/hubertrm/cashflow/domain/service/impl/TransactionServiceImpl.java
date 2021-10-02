package be.hubertrm.cashflow.domain.service.impl;

import be.hubertrm.cashflow.domain.exception.ResourceNotFoundException;
import be.hubertrm.cashflow.domain.model.Transaction;
import be.hubertrm.cashflow.domain.repository.TransactionRepository;
import be.hubertrm.cashflow.domain.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    @Resource
    private TransactionRepository transactionRepository;

    private static final String RECORD_NOT_FOUND_MESSAGE = "Transaction not found for this id :: %s";

    @Override
    public List<Transaction> getAll() {
        return transactionRepository.getAll();
    }

    @Override
    public Transaction getById(Long id) throws ResourceNotFoundException {
        return transactionRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RECORD_NOT_FOUND_MESSAGE, id));
    }

    @Override
    public Long create(final Transaction rec) {
        final var transactionId = transactionRepository.save(rec);

        log.debug("Created transaction with id [{}]", transactionId);
        return transactionId;
    }

    @Override
    public List<Long> create(final List<Transaction> transactions) {
        final var transactionIds = transactionRepository.save(transactions);

        log.debug("Created transactions with ids [{}]", transactionIds);
        return transactionIds;
    }

    @Override
    public void update(Long id, Transaction model) throws ResourceNotFoundException {
        Transaction current = transactionRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(RECORD_NOT_FOUND_MESSAGE, id));
        transactionRepository.update(current.getId(), model);
        log.debug("Updated transaction with id [{}]", id);
    }

    @Override
    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
        log.debug("Deleted transaction with id [{}]", id);
    }
}

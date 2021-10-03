package be.hubertrm.cashflow.domain.service.impl;

import be.hubertrm.cashflow.domain.exception.ResourceNotFoundException;
import be.hubertrm.cashflow.domain.model.Transaction;
import be.hubertrm.cashflow.domain.repository.TransactionRepository;
import be.hubertrm.cashflow.domain.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    @Resource
    private TransactionRepository transactionRepository;

    private static final String TRANSACTION_NOT_FOUND_MESSAGE = "Transaction not found for this id :: %s";

    @Override
    public boolean exists(Long id) { return transactionRepository.findById(id).isPresent(); }

    @Override
    public List<Transaction> getAll() {
        return transactionRepository.getAll();
    }

    @Override
    public Transaction getById(Long id) throws ResourceNotFoundException {
        return transactionRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(TRANSACTION_NOT_FOUND_MESSAGE, id));
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
                new ResourceNotFoundException(TRANSACTION_NOT_FOUND_MESSAGE, id));
        transactionRepository.update(current.getId(), model);
        log.debug("Updated transaction with id [{}]", id);
    }

    @Override
    public void deleteById(Long id) throws ResourceNotFoundException {
        Optional<Transaction> optionalEntity = transactionRepository.findById(id);
        if(optionalEntity.isPresent()) {
            transactionRepository.deleteById(id);
            log.debug("Deleted transaction with id [{}]", id);
        } else {
            throw new ResourceNotFoundException(TRANSACTION_NOT_FOUND_MESSAGE, id);
        }
    }
}

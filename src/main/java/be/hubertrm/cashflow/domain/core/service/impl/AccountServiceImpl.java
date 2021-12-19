package be.hubertrm.cashflow.domain.core.service.impl;

import be.hubertrm.cashflow.domain.core.exception.ResourceNotFoundException;
import be.hubertrm.cashflow.domain.core.model.Account;
import be.hubertrm.cashflow.domain.core.model.Category;
import be.hubertrm.cashflow.domain.core.repository.AccountRepository;
import be.hubertrm.cashflow.domain.core.service.AccountService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountRepository accountRepository;

    private static final String ACCOUNT_NOT_FOUND_MESSAGE = "Account not found for this id :: %s";
    private static final String ACCOUNT_NOT_FOUND_BY_NAME_MESSAGE = "Account not found for this name :: %s";

    @Override
    public boolean exists(Long id) { return accountRepository.findById(id).isPresent(); }

    @Override
    public List<Account> getAll() {
        return accountRepository.getAll();
    }

    @Override
    public Account getById(Long accountId) throws ResourceNotFoundException {
        return accountRepository.findById(accountId).orElseThrow(() ->
                new ResourceNotFoundException(ACCOUNT_NOT_FOUND_MESSAGE, accountId));
    }

    @Override
    public Account getByName(String name) throws ResourceNotFoundException {
        return accountRepository.findByName(name).orElseThrow(() ->
                new ResourceNotFoundException(ACCOUNT_NOT_FOUND_BY_NAME_MESSAGE, name));
    }

    @Override
    public Long create(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public List<Long> createAll(List<Account> accountList) {
        return accountRepository.saveAll(accountList);
    }

    @Override
    public void update(Long accountId, Account accountDetails) throws ResourceNotFoundException {
        Account current = accountRepository.findById(accountId).orElseThrow(() ->
                new ResourceNotFoundException(ACCOUNT_NOT_FOUND_MESSAGE, accountId));
        accountRepository.update(current.getId(), accountDetails);
    }

    @Override
    public void deleteById(Long accountId) throws ResourceNotFoundException  {
        Optional<Account> optionalEntity = accountRepository.findById(accountId);
        if(optionalEntity.isPresent()) {
            accountRepository.deleteById(accountId);
        } else {
            throw new ResourceNotFoundException(ACCOUNT_NOT_FOUND_MESSAGE, accountId);
        }
    }
}

package be.hubertrm.cashflow.infra.repository.impl;

import be.hubertrm.cashflow.domain.core.model.Account;
import be.hubertrm.cashflow.domain.core.repository.AccountRepository;
import be.hubertrm.cashflow.infra.entity.AccountEntity;
import be.hubertrm.cashflow.infra.repository.JpaAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class AccountRepositoryImpl implements AccountRepository {

    private final JpaAccountRepository repository;

    public AccountRepositoryImpl(JpaAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Account> findById(Long id) {
        return repository.findById(id).map(AccountEntity::fromThis);
    }

    @Override
    public Optional<Account> findByName(String name) {
        return repository.findByName(name).map(AccountEntity::fromThis);
    }

    @Override
    public List<Account> getAll() {
        return repository.findAll().stream().map(AccountEntity::fromThis).collect(Collectors.toList());
    }

    @Override
    public Long save(Account account) {
        return repository.save(AccountEntity.from(account)).getId();
    }

    @Override
    public void update(Long accountId, Account account) {
        account.setId(accountId);
        save(account);
    }

    @Override
    public void deleteById(Long accountId) {
        repository.deleteById(accountId);
    }
}

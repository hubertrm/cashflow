package be.hubertrm.cashflow.domain.core.service;

import be.hubertrm.cashflow.domain.core.model.Account;

import java.util.Optional;

public interface AccountService extends Service<Account>, Named<Account> {

    Optional<Account> findByName(String name);
}

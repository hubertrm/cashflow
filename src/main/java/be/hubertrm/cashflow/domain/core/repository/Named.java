package be.hubertrm.cashflow.domain.core.repository;

import java.util.Optional;

public interface Named<T> {

    Optional<T> findByName(String name);
}

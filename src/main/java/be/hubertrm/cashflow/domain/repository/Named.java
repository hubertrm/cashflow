package be.hubertrm.cashflow.domain.repository;

import java.util.Optional;

public interface Named<T> {

    Optional<T> findByName(String name);
}

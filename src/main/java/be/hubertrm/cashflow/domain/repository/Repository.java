package be.hubertrm.cashflow.domain.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<M> {

    Optional<M> findById(Long id);
    
    List<M> getAll();

    Long save(M m);

    void update(Long id, M m);

    void deleteById(Long id);
}

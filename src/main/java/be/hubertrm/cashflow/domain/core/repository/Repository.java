package be.hubertrm.cashflow.domain.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface Repository<M> {

    Optional<M> findById(Long id);
    
    List<M> getAll();

    Page<M> getAll(Pageable pageable);

    Long save(M m);

    List<Long> saveAll(List<M> m);

    void update(Long id, M m);

    void deleteById(Long id);
}

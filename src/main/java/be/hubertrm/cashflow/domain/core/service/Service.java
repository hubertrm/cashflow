package be.hubertrm.cashflow.domain.core.service;

import be.hubertrm.cashflow.domain.core.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface Service<T> {

    boolean exists(Long id);

    List<T> getAll();

    Page<T> getAll(Pageable pageable);

    T getById(Long id) throws ResourceNotFoundException;

    Long create(T model);

    List<Long> createAll(List<T> modelList);

    void update(Long id, T model) throws ResourceNotFoundException;

    void deleteById(Long id) throws ResourceNotFoundException;
}

package be.hubertrm.cashflow.domain.service;

import be.hubertrm.cashflow.domain.exception.ResourceNotFoundException;

import java.util.List;

public interface Service<T> {

    boolean exists(Long id);

    List<T> getAll();

    T getById(Long id) throws ResourceNotFoundException;

    Long create(T model);

    void update(Long id, T model) throws ResourceNotFoundException;

    void deleteById(Long id) throws ResourceNotFoundException;
}

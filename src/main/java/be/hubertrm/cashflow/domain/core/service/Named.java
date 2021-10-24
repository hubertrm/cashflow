package be.hubertrm.cashflow.domain.core.service;

import be.hubertrm.cashflow.domain.core.exception.ResourceNotFoundException;

public interface Named<T> {

    T getByName(String name) throws ResourceNotFoundException;
}

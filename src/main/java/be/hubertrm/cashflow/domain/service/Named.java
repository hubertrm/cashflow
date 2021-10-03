package be.hubertrm.cashflow.domain.service;

import be.hubertrm.cashflow.domain.exception.ResourceNotFoundException;

public interface Named<T> {

    T getByName(String name) throws ResourceNotFoundException;
}

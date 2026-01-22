package be.hubertrm.cashflow.domain.core.service;

import be.hubertrm.cashflow.domain.core.model.Category;

import java.util.Optional;

public interface CategoryService extends Service<Category>, Named<Category> {

    Optional<Category> findByName(String name);
}

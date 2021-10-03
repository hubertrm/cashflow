package be.hubertrm.cashflow.infra.repository;

import be.hubertrm.cashflow.infra.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface JpaCategoryRepository extends JpaRepository<CategoryEntity, Long> {

    @Query("select ca from CategoryEntity ca where ca.name = ?1")
    Optional<CategoryEntity> findByName(String name);
}

package be.hubertrm.cashflow.infra.repository;

import be.hubertrm.cashflow.infra.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface JpaAccountRepository extends JpaRepository<AccountEntity, Long> {

    @Query("select ca from AccountEntity ca where ca.name = ?1")
    Optional<AccountEntity> findByName(String name);
}

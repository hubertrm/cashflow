package be.hubertrm.cashflow.infra.repository;

import be.hubertrm.cashflow.infra.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTransactionRepository extends JpaRepository<TransactionEntity, Long> {
}

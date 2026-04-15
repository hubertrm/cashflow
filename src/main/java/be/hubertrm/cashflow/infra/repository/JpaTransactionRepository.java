package be.hubertrm.cashflow.infra.repository;

import be.hubertrm.cashflow.infra.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface JpaTransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query("SELECT t FROM TransactionEntity t WHERE t.date = ?1 AND t.amount = ?2 " +
           "AND t.category.id = ?3 AND t.account.id = ?4 " +
            "AND (t.description = ?5 OR (t.description IS NULL AND ?5 IS NULL)) AND t.reference = ?6")
    Optional<TransactionEntity> findDuplicate(LocalDate date, Float amount, Long categoryId, Long accountId, String description, Long reference);
}

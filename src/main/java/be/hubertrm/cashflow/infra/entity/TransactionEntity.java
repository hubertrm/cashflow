package be.hubertrm.cashflow.infra.entity;

import be.hubertrm.cashflow.domain.model.Transaction;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transaction")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "amount", nullable = false)
    private Float amount;

    @JoinColumn(name = "category_id")
    @ManyToOne
    private CategoryEntity category;

    @JoinColumn(name = "account_id")
    @ManyToOne
    private AccountEntity account;

    @Column(name = "description")
    private String description;

    public static TransactionEntity from(Transaction transaction) {
        return new TransactionEntity(
                transaction.getId(),
                transaction.getDate(),
                transaction.getAmount(),
                CategoryEntity.from(transaction.getCategory()),
                AccountEntity.from(transaction.getAccount()),
                transaction.getDescription()
        );
    }

    public Transaction fromThis() {
        return new Transaction(id, date, amount, category.fromThis(), account.fromThis(), description);
    }
}

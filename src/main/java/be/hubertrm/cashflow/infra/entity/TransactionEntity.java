package be.hubertrm.cashflow.infra.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "transaction")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "amount", nullable = false)
    private Float amount;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "description")
    private String description;
}

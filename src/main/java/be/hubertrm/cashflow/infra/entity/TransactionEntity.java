package be.hubertrm.cashflow.infra.entity;

import be.hubertrm.cashflow.domain.core.model.Transaction;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "week_number")
    private Integer weekNumber;

    @Column(name = "holiday")
    private String holiday;

    @Column(name = "month")
    private String month;

    @Column(name = "ticker")
    private String ticker;

    @Column(name = "nbr_of_actions")
    private Integer nbrOfActions;

    @Column(name = "change_rate")
    private Float changeRate;

    @Column(name = "is_common")
    private Boolean isCommon;

    @Column(name = "before_conversion")
    private Float beforeConversion;

    @Column(name = "currency")
    private String currency;

    @Column(name = "year")
    private Integer year;

    @Column(name = "reference")
    private Long reference;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "transaction_tags", joinColumns = @JoinColumn(name = "transaction_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    public static TransactionEntity from(Transaction transaction) {
        TransactionEntity entity = new TransactionEntity();
        entity.setId(transaction.getId());
        entity.setDate(transaction.getDate());
        entity.setAmount(transaction.getAmount());
        entity.setCategory(transaction.getCategory() != null ? CategoryEntity.from(transaction.getCategory()) : null);
        entity.setAccount(transaction.getAccount() != null ? AccountEntity.from(transaction.getAccount()) : null);
        entity.setDescription(transaction.getDescription());
        entity.setWeekNumber(transaction.getWeekNumber());
        entity.setHoliday(transaction.getHoliday());
        entity.setMonth(transaction.getMonth());
        entity.setTicker(transaction.getTicker());
        entity.setNbrOfActions(transaction.getNbrOfActions());
        entity.setChangeRate(transaction.getChangeRate());
        entity.setIsCommon(transaction.getIsCommon());
        entity.setBeforeConversion(transaction.getBeforeConversion());
        entity.setCurrency(transaction.getCurrency());
        entity.setYear(transaction.getYear());
        entity.setTags(transaction.getTags() != null ? new ArrayList<>(transaction.getTags()) : new ArrayList<>());
        return entity;
    }

    public Transaction fromThis() {
        return new Transaction(
                id, date, amount,
                category != null ? category.fromThis() : null,
                account != null ? account.fromThis() : null,
                description, weekNumber, holiday, month, ticker,
                nbrOfActions, changeRate, isCommon, beforeConversion, currency, year, reference,
                tags != null ? new ArrayList<>(tags) : new ArrayList<>()
        );
    }
}

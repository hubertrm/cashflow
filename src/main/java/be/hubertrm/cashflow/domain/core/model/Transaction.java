package be.hubertrm.cashflow.domain.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    private Long id;
    private LocalDate date;
    private float amount;
    private Category category;
    private Account account;
    private String description;
    private Integer weekNumber;
    private String holiday;
    private String month;
    private String ticker;
    private Integer nbrOfActions;
    private Float changeRate;
    private Boolean isCommon;
    private Float beforeConversion;
    private String currency;
    private Integer year;
    private Long reference;
    private List<String> tags;

    public Transaction(Long id, LocalDate date, float amount, Category category, Account account, String description) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.account = account;
        this.description = description;
    }
}

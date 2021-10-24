package be.hubertrm.cashflow.domain.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class Transaction {

    private Long id;
    private LocalDate date;
    private float amount;
    private Category category;
    private Account account;
    private String description;

}

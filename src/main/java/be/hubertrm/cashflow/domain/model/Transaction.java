package be.hubertrm.cashflow.domain.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Transaction {

    private LocalDate date;
    private float amount;
    private Category category;
    private Account account;
    private String description;

}

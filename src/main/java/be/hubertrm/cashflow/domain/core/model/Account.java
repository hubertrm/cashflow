package be.hubertrm.cashflow.domain.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Account {

    private Long id;
    private String name;
    private LocalDate date;
}

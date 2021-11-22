package be.hubertrm.cashflow.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AccountDto {

    private Long id;
    private String name;
    private LocalDate date;
}

package be.hubertrm.cashflow.facade.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class TransactionDto {

    private Long id;
    private LocalDate date;
    private float amount;
    private CategoryDto category;
    private AccountDto account;
    private String description;

}

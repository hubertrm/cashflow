package be.hubertrm.cashflow.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {

    private Long id;
    private LocalDate date;
    private float amount;
    private CategoryDto category;
    private AccountDto account;
    private String description;

}

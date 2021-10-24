package be.hubertrm.cashflow.domain.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
//@Accessors(chain = true)
@AllArgsConstructor
public class Category {

    private Long id;
    private String name;
    private LocalDate date;
}

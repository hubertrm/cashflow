package be.hubertrm.cashflow.facade.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class CategoryDto {

    private Long id;
    private String name;
    private LocalDate date;
}

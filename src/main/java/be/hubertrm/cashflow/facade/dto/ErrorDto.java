package be.hubertrm.cashflow.facade.dto;

import be.hubertrm.cashflow.domain.file.enums.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDto {
    private ErrorType errorType;
    private String message;
}

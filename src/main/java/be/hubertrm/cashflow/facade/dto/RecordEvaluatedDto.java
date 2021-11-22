package be.hubertrm.cashflow.facade.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class RecordEvaluatedDto {

    private EvaluationDto date;
    private EvaluationDto amount;
    private EvaluationDto category;
    private EvaluationDto account;
    private EvaluationDto description;

}

package be.hubertrm.cashflow.domain.file.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class RecordEvaluated {

    private Evaluation date;
    private Evaluation amount;
    private Evaluation category;
    private Evaluation account;
    private Evaluation description;

}

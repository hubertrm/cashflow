package be.hubertrm.cashflow.domain.sheets.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class RangeOptions {

    private String majorDimension;
    private String valueRenderOption;
}

package be.hubertrm.cashflow.application.dto;

import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class RangeDto {

    private List<RowDto> rows;

    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RowDto {
        private List<CellDto> cells;
    }

    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public abstract static class CellDto {
        protected String type;
        protected Object value;

        public CellDto(Object value) {
            this.value = value;
        }

        public static CellDto from(Object value) {
            return switch (value) {
                case BigDecimal b -> new BigDecimalCellDto(b);
                case String s -> new StringCellDto(s);
                default -> null;
            };
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    public static class BigDecimalCellDto extends CellDto {

        public BigDecimalCellDto(Object value) {
            super(value);
            this.setType(CellType.BIG_DECIMAL.getValue());
        }

        @Override
        public BigDecimal getValue() {
            return (BigDecimal) super.value;
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    public static class StringCellDto extends CellDto {

        public StringCellDto(Object value) {
            super(value);
            this.setType(CellType.STRING.getValue());
        }

        @Override
        public String getValue() {
            return (String) super.value;
        }
    }

    @Getter
    enum CellType {
        BIG_DECIMAL("number"),
        STRING("string");

        private final String value;

        CellType(String value) {
            this.value = value;
        }
    }
}

package be.hubertrm.cashflow.application.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RangeDtoTest {

    @Nested
    class CellDtoFromDesigns {

        @Test
        @DisplayName("CellDto.from creates BigDecimalCellDto for BigDecimal values")
        void testFromBigDecimal() {
            RangeDto.CellDto cell = RangeDto.CellDto.from(new BigDecimal("100.50"));

            assertThat(cell).isInstanceOf(RangeDto.BigDecimalCellDto.class);
            assertThat(cell.getValue()).isEqualTo(new BigDecimal("100.50"));
            assertThat(cell.getType()).isEqualTo("number");
        }

        @Test
        @DisplayName("CellDto.from creates StringCellDto for String values")
        void testFromString() {
            RangeDto.CellDto cell = RangeDto.CellDto.from("hello");

            assertThat(cell).isInstanceOf(RangeDto.StringCellDto.class);
            assertThat(cell.getValue()).isEqualTo("hello");
            assertThat(cell.getType()).isEqualTo("string");
        }

        @Test
        @DisplayName("CellDto.from returns null for unsupported types")
        void testFromUnsupported() {
            RangeDto.CellDto cell = RangeDto.CellDto.from(42);

            assertThat(cell).isNull();
        }
    }

    @Nested
    class RowDtoDesigns {

        @Test
        @DisplayName("RowDto can be constructed with cells")
        void testRowDto() {
            RangeDto.CellDto cell = RangeDto.CellDto.from("test");
            RangeDto.RowDto row = new RangeDto.RowDto(List.of(cell));

            assertThat(row.getCells()).hasSize(1);
        }
    }

    @Nested
    class RangeDtoDesigns {

        @Test
        @DisplayName("RangeDto can be constructed with rows")
        void testRangeDto() {
            RangeDto.CellDto cell = RangeDto.CellDto.from("test");
            RangeDto.RowDto row = new RangeDto.RowDto(List.of(cell));
            RangeDto range = new RangeDto(List.of(row));

            assertThat(range.getRows()).hasSize(1);
        }
    }
}
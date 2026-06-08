package be.hubertrm.cashflow.application.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RangeMapperTest {

    @Nested
    class toRangeDtoDesigns {

        @Test
        @DisplayName("toRangeDto maps nested lists to RangeDto")
        void testToRangeDto() {
            List<List<Object>> raw = List.of(
                    List.of("header1", new BigDecimal("100"), "header3"),
                    List.of("value1", new BigDecimal("200"), "value3")
            );

            var result = RangeMapper.toRangeDto(raw);

            assertThat(result.getRows()).hasSize(2);
            assertThat(result.getRows().get(0).getCells()).hasSize(3);
            assertThat(result.getRows().get(1).getCells()).hasSize(3);
        }

        @Test
        @DisplayName("toRangeDto handles empty list")
        void testToRangeDtoEmpty() {
            var result = RangeMapper.toRangeDto(List.of());

            assertThat(result.getRows()).isEmpty();
        }
    }
}
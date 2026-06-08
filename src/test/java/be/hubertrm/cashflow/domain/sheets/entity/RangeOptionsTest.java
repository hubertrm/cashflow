package be.hubertrm.cashflow.domain.sheets.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RangeOptionsTest {

    @Nested
    class constructorDesigns {

        @Test
        @DisplayName("AllArgsConstructor creates RangeOptions with values")
        void testAllArgsConstructor() {
            RangeOptions options = new RangeOptions("ROWS", "FORMATTED_VALUE");

            assertThat(options.getMajorDimension()).isEqualTo("ROWS");
            assertThat(options.getValueRenderOption()).isEqualTo("FORMATTED_VALUE");
        }

        @Test
        @DisplayName("NoArgsConstructor creates empty RangeOptions")
        void testNoArgsConstructor() {
            RangeOptions options = new RangeOptions();

            assertThat(options.getMajorDimension()).isNull();
            assertThat(options.getValueRenderOption()).isNull();
        }

        @Test
        @DisplayName("Fluent setters work correctly")
        void testFluentSetters() {
            RangeOptions options = new RangeOptions()
                    .setMajorDimension("COLUMNS")
                    .setValueRenderOption("FORMULA");

            assertThat(options.getMajorDimension()).isEqualTo("COLUMNS");
            assertThat(options.getValueRenderOption()).isEqualTo("FORMULA");
        }
    }
}
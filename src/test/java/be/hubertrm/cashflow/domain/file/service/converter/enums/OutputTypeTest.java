package be.hubertrm.cashflow.domain.file.service.converter.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OutputTypeTest {

    @Nested
    class enumValuesDesigns {

        @Test
        @DisplayName("OutputType has LIST and ARRAY values")
        void testValues() {
            assertThat(OutputType.values()).containsExactly(OutputType.LIST, OutputType.ARRAY);
        }

        @Test
        @DisplayName("OutputType.LIST name is LIST")
        void testList() {
            assertThat(OutputType.LIST.name()).isEqualTo("LIST");
        }

        @Test
        @DisplayName("OutputType.ARRAY name is ARRAY")
        void testArray() {
            assertThat(OutputType.ARRAY.name()).isEqualTo("ARRAY");
        }
    }
}
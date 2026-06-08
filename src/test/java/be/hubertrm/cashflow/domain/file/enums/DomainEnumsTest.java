package be.hubertrm.cashflow.domain.file.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DomainEnumsTest {

    @Nested
    class DatePatternDesigns {

        @Test
        @DisplayName("DEFAULT_DATE_PATTERN returns correct value")
        void testDefaultDatePattern() {
            assertThat(DatePattern.DEFAULT_DATE_PATTERN.getValue()).isEqualTo("default-date-pattern");
        }
    }

    @Nested
    class ErrorTypeDesigns {

        @Test
        @DisplayName("ErrorType values are correct")
        void testErrorTypeValues() {
            assertThat(ErrorType.values()).containsExactly(
                    ErrorType.FIELD_DOES_NOT_EXIST,
                    ErrorType.WRONG_FORMAT,
                    ErrorType.MISSING_VALUE
            );
        }

        @Test
        @DisplayName("ErrorType name matches enum constant")
        void testErrorTypeNames() {
            assertThat(ErrorType.FIELD_DOES_NOT_EXIST.name()).isEqualTo("FIELD_DOES_NOT_EXIST");
            assertThat(ErrorType.WRONG_FORMAT.name()).isEqualTo("WRONG_FORMAT");
            assertThat(ErrorType.MISSING_VALUE.name()).isEqualTo("MISSING_VALUE");
        }
    }

    @Nested
    class FileTypeDesigns {

        @Test
        @DisplayName("FileType returns correct file type IDs")
        void testFileTypeIds() {
            assertThat(FileType.CSV.getFileTypeId()).isEqualTo("CSV_FILE_TYPE");
            assertThat(FileType.CSV_STRING.getFileTypeId()).isEqualTo("CSV_STRING_TYPE");
            assertThat(FileType.JSON.getFileTypeId()).isEqualTo("JSON_FILE_TYPE");
        }

        @Test
        @DisplayName("FileType values are preserved")
        void testFileTypeValues() {
            assertThat(FileType.values()).hasSize(3);
        }
    }
}
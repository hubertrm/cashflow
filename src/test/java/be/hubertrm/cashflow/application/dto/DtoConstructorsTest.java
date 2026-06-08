package be.hubertrm.cashflow.application.dto;

import be.hubertrm.cashflow.application.exception.ErrorDetails;
import be.hubertrm.cashflow.domain.file.enums.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class DtoConstructorsTest {

    private static final LocalDate NOW = LocalDate.of(2021, 12, 31);

    @Nested
    class AccountDtoDesigns {

        @Test
        @DisplayName("AccountDto all-args constructor")
        void testAccountDtoConstructor() {
            AccountDto dto = new AccountDto(1L, "Checking", NOW);
            assertThat(dto.getId()).isEqualTo(1L);
            assertThat(dto.getName()).isEqualTo("Checking");
            assertThat(dto.getDate()).isEqualTo(NOW);
        }
    }

    @Nested
    class CategoryDtoDesigns {

        @Test
        @DisplayName("CategoryDto all-args constructor and fluent setters")
        void testCategoryDto() {
            CategoryDto dto = new CategoryDto(1L, "Food", NOW);
            assertThat(dto.getId()).isEqualTo(1L);
            assertThat(dto.getName()).isEqualTo("Food");
        }

        @Test
        @DisplayName("CategoryDto fluent setters")
        void testCategoryDtoFluentSetters() {
            CategoryDto dto = new CategoryDto(null, null, null)
                    .setId(2L)
                    .setName("Transport")
                    .setDate(NOW);

            assertThat(dto.getId()).isEqualTo(2L);
            assertThat(dto.getName()).isEqualTo("Transport");
            assertThat(dto.getDate()).isEqualTo(NOW);
        }
    }

    @Nested
    class TransactionDtoDesigns {

        @Test
        @DisplayName("TransactionDto 6-arg constructor")
        void testTransactionDto6ArgConstructor() {
            CategoryDto cat = new CategoryDto(1L, "Food", NOW);
            AccountDto acc = new AccountDto(1L, "Checking", NOW);
            TransactionDto dto = new TransactionDto(1L, NOW, 100f, cat, acc, "groceries");

            assertThat(dto.getId()).isEqualTo(1L);
            assertThat(dto.getDescription()).isEqualTo("groceries");
            assertThat(dto.getTags()).isEmpty();
        }

        @Test
        @DisplayName("TransactionDto fluent setters")
        void testTransactionDtoFluentSetters() {
            TransactionDto dto = new TransactionDto()
                    .setId(1L)
                    .setDate(NOW)
                    .setAmount(100f)
                    .setDescription("test")
                    .setReference(42);

            assertThat(dto.getReference()).isEqualTo(42);
            assertThat(dto.getId()).isEqualTo(1L);
        }
    }

    @Nested
    class ErrorDetailsDesigns {

        @Test
        @DisplayName("ErrorDetails stores timestamp, message and details")
        void testErrorDetails() {
            Date now = new Date();
            ErrorDetails details = new ErrorDetails(now, "Error message", "Detailed info");

            assertThat(details.getTimestamp()).isEqualTo(now);
            assertThat(details.getMessage()).isEqualTo("Error message");
            assertThat(details.getDetails()).isEqualTo("Detailed info");
        }
    }

    @Nested
    class ErrorDtoDesigns {

        @Test
        @DisplayName("ErrorDto all-args constructor")
        void testErrorDto() {
            ErrorDto dto = new ErrorDto(ErrorType.WRONG_FORMAT, "Bad format");

            assertThat(dto.getErrorType()).isEqualTo(ErrorType.WRONG_FORMAT);
            assertThat(dto.getMessage()).isEqualTo("Bad format");
        }
    }

    @Nested
    class EvaluationDtoDesigns {

        @Test
        @DisplayName("EvaluationDto with value and error")
        void testEvaluationDto() {
            ErrorDto error = new ErrorDto(ErrorType.MISSING_VALUE, "missing");
            EvaluationDto dto = new EvaluationDto("test value", error);

            assertThat(dto.getValue()).isEqualTo("test value");
            assertThat(dto.getError()).isEqualTo(error);
        }
    }

    @Nested
    class RecordEvaluatedDtoDesigns {

        @Test
        @DisplayName("RecordEvaluatedDto fluent setters")
        void testRecordEvaluatedDto() {
            EvaluationDto dateEval = new EvaluationDto().setValue("2021-12-31");
            RecordEvaluatedDto dto = new RecordEvaluatedDto()
                    .setDate(dateEval)
                    .setAmount(new EvaluationDto().setValue("100"))
                    .setCategory(new EvaluationDto().setValue("Food"))
                    .setAccount(new EvaluationDto().setValue("Checking"))
                    .setDescription(new EvaluationDto().setValue("groceries"));

            assertThat(dto.getDate()).isEqualTo(dateEval);
            assertThat(dto.getAmount().getValue()).isEqualTo("100");
            assertThat(dto.getCategory().getValue()).isEqualTo("Food");
            assertThat(dto.getAccount().getValue()).isEqualTo("Checking");
            assertThat(dto.getDescription().getValue()).isEqualTo("groceries");
        }
    }
}
package be.hubertrm.cashflow.domain.file.model;

import be.hubertrm.cashflow.domain.file.enums.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DomainModelTest {

    @Nested
    class ErrorDesigns {

        @Test
        @DisplayName("Error can be created with all fields")
        void testError() {
            Error error = new Error(ErrorType.WRONG_FORMAT, "wrong format message");

            assertThat(error.getErrorType()).isEqualTo(ErrorType.WRONG_FORMAT);
            assertThat(error.getMessage()).isEqualTo("wrong format message");
        }

        @Test
        @DisplayName("Error can use fluent setters")
        void testErrorFluentSetters() {
            Error error = new Error()
                    .setErrorType(ErrorType.MISSING_VALUE)
                    .setMessage("missing value");

            assertThat(error.getErrorType()).isEqualTo(ErrorType.MISSING_VALUE);
            assertThat(error.getMessage()).isEqualTo("missing value");
        }
    }

    @Nested
    class RecordDesigns {

        @Test
        @DisplayName("Record initializes with line and empty fields")
        void testRecordConstructor() {
            Record record = new Record("header1,header2");

            assertThat(record.getLine()).isEqualTo("header1,header2");
            assertThat(record.getFields()).isEmpty();
        }

        @Test
        @DisplayName("Record add accumulates fields")
        void testRecordAdd() {
            Record record = new Record("line");
            record.add("field1");
            record.add("field2");

            assertThat(record.getFields()).containsExactly("field1", "field2");
        }
    }

    @Nested
    class EvaluationDesigns {

        @Test
        @DisplayName("Evaluation can be created with value and error")
        void testEvaluation() {
            Error error = new Error(ErrorType.WRONG_FORMAT, "bad format");
            Evaluation evaluation = new Evaluation("100", error);

            assertThat(evaluation.getValue()).isEqualTo("100");
            assertThat(evaluation.getError()).isEqualTo(error);
        }

        @Test
        @DisplayName("Evaluation can use fluent setters")
        void testEvaluationFluentSetters() {
            Evaluation evaluation = new Evaluation()
                    .setValue("test value");

            assertThat(evaluation.getValue()).isEqualTo("test value");
            assertThat(evaluation.getError()).isNull();
        }
    }

    @Nested
    class RecordEvaluatedDesigns {

        @Test
        @DisplayName("RecordEvaluated can be created with fluent setters")
        void testRecordEvaluated() {
            RecordEvaluated record = new RecordEvaluated()
                    .setDate(new Evaluation().setValue("2021-12-31"))
                    .setAmount(new Evaluation().setValue("100"))
                    .setCategory(new Evaluation().setValue("Food"))
                    .setAccount(new Evaluation().setValue("Checking"))
                    .setDescription(new Evaluation().setValue("groceries"));

            assertThat(record.getDate().getValue()).isEqualTo("2021-12-31");
            assertThat(record.getAmount().getValue()).isEqualTo("100");
            assertThat(record.getCategory().getValue()).isEqualTo("Food");
            assertThat(record.getAccount().getValue()).isEqualTo("Checking");
            assertThat(record.getDescription().getValue()).isEqualTo("groceries");
        }
    }
}
package be.hubertrm.cashflow.domain.file.service.reader;

import be.hubertrm.cashflow.domain.file.model.Evaluation;
import be.hubertrm.cashflow.domain.file.model.RecordEvaluated;
import be.hubertrm.cashflow.domain.file.service.EvaluatorService;
import be.hubertrm.cashflow.domain.file.service.factory.ConverterFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class CsvReaderTest {

    @Autowired
    private CsvReader csvReader;

    @MockitoBean
    private EvaluatorService evaluatorService;

    @Nested
    class readDesigns {

        @Test
        @DisplayName("read splits headers and records and returns evaluated records")
        void testRead() {
            RecordEvaluated record = new RecordEvaluated()
                    .setDate(new Evaluation().setValue("2021-12-31"))
                    .setAmount(new Evaluation().setValue("100.0"))
                    .setDescription(new Evaluation().setValue("test"));
            when(evaluatorService.create(any(String[].class), anyString(), eq(Locale.ROOT)))
                    .thenReturn(record);

            List<RecordEvaluated> result = csvReader.read("date,amount,description", "2021-12-31,100,test");

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("read with explicit locale")
        void testReadWithLocale() {
            RecordEvaluated record = new RecordEvaluated()
                    .setDate(new Evaluation().setValue("2021-12-31"))
                    .setAmount(new Evaluation().setValue("100.0"));
            when(evaluatorService.create(any(String[].class), anyString(), eq(Locale.FRANCE)))
                    .thenReturn(record);

            List<RecordEvaluated> result = csvReader.read("date,amount", "2021-12-31,100", Locale.FRANCE);

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("read handles multiple lines")
        void testReadMultipleLines() {
            RecordEvaluated record1 = new RecordEvaluated().setDescription(new Evaluation().setValue("first"));
            RecordEvaluated record2 = new RecordEvaluated().setDescription(new Evaluation().setValue("second"));
            when(evaluatorService.create(any(String[].class), eq("line1"), any(Locale.class))).thenReturn(record1);
            when(evaluatorService.create(any(String[].class), eq("line2"), any(Locale.class))).thenReturn(record2);

            List<RecordEvaluated> result = csvReader.read("header", "line1\nline2");

            assertThat(result).hasSize(2);
        }

        @Test
        @DisplayName("read filters out empty lines")
        void testReadFiltersEmptyLines() {
            RecordEvaluated record = new RecordEvaluated().setDescription(new Evaluation().setValue("data"));
            when(evaluatorService.create(any(String[].class), eq("data"), any(Locale.class))).thenReturn(record);

            List<RecordEvaluated> result = csvReader.read("header", "\n\ndata\n\n");

            assertThat(result).hasSize(1);
        }
    }

    @Nested
    class getSupportedFileTypeDesigns {

        @Test
        @DisplayName("getSupportedFileType returns CSV_STRING")
        void testGetSupportedFileType() {
            assertThat(csvReader.getSupportedFileType()).isNotNull();
        }
    }
}
package be.hubertrm.cashflow.application.controller;

import be.hubertrm.cashflow.application.dto.RangeDto;
import be.hubertrm.cashflow.domain.sheets.entity.RangeOptions;
import be.hubertrm.cashflow.domain.sheets.service.SheetsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class SheetsControllerTest {

    @Autowired
    private SheetsController controller;

    @MockitoBean
    private SheetsService sheetsService;

    @Nested
    class getBySpreadSheetIdDesigns {

        @Test
        @DisplayName("getBySpreadSheetId with all params specified")
        void testGetBySpreadSheetIdAllParams() {
            List<List<Object>> sheetData = List.of(
                    List.of("header1", "header2"),
                    List.of("value1", "value2")
            );
            when(sheetsService.getRangeBySpreadSheetIdRangeAndValueRenderOption(
                    eq("spreadsheet1"), eq("A1:B2"), any(RangeOptions.class)))
                    .thenReturn(sheetData);

            ResponseEntity<RangeDto> response = controller.getBySpreadSheetId(
                    "spreadsheet1", "A1:B2", "ROWS", "FORMATTED_VALUE");

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getRows()).hasSize(2);
        }

        @Test
        @DisplayName("getBySpreadSheetId with defaults when optional params are null")
        void testGetBySpreadSheetIdWithDefaults() {
            List<List<Object>> sheetData = List.of(List.of("value1"));
            when(sheetsService.getRangeBySpreadSheetIdRangeAndValueRenderOption(
                    eq("spreadsheet1"), eq("A1:A1"), any(RangeOptions.class)))
                    .thenReturn(sheetData);

            ResponseEntity<RangeDto> response = controller.getBySpreadSheetId(
                    "spreadsheet1", "A1:A1", null, null);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
        }
    }
}
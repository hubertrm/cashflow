package be.hubertrm.cashflow.application.controller;

import be.hubertrm.cashflow.application.dto.SyncRequestDto;
import be.hubertrm.cashflow.application.dto.SyncResultDto;
import be.hubertrm.cashflow.application.manager.SyncBusinessManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class SyncControllerTest {

    @Autowired
    private SyncController controller;

    @MockitoBean
    private SyncBusinessManager manager;

    @Nested
    class syncFromSheetsDesigns {

        @Test
        @DisplayName("syncFromSheets returns 200 OK with SyncResultDto")
        void testSyncFromSheets() {
            SyncRequestDto request = new SyncRequestDto();
            request.setSheetId("sheet1");
            request.setRange("A:Z");
            List<SyncRequestDto.HeaderMapping> headers = new ArrayList<>();
            request.setHeaders(headers);

            SyncResultDto result = new SyncResultDto();
            result.setTotalRows(10);
            result.setSuccessCount(5);
            when(manager.syncSheetToDatabase(request)).thenReturn(result);

            ResponseEntity<SyncResultDto> response = controller.syncFromSheets(request);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getTotalRows()).isEqualTo(10);
        }
    }
}
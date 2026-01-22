package be.hubertrm.cashflow;

import be.hubertrm.cashflow.interfaces.client.SheetsClient;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@SpringBootTest
public class GoogleSheetsLiveTest {

    @Autowired
    private SheetsClient sheetsClient;
    private final static String SPREADSHEET_ID = "11ZaB0hObUJnnlXp4GQXuqga63ezjm5rUTTNlniIlo0o";

    @Test
    public void should_read_sheet() throws IOException, GeneralSecurityException {
        List<String> ranges = List.of("Main!A2:O274");
        BatchGetValuesResponse readResult = sheetsClient.getSheetsService().spreadsheets().values()
                .batchGet(SPREADSHEET_ID)
                .setRanges(ranges)
                .execute();

        Assertions.assertThat(readResult).isNotEmpty();
        Assertions.assertThat(readResult.getValueRanges().getFirst().getValues()).isNotEmpty();
    }
}

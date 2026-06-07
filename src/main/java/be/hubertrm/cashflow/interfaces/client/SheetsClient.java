package be.hubertrm.cashflow.interfaces.client;

import be.hubertrm.cashflow.domain.sheets.entity.RangeOptions;
import be.hubertrm.cashflow.interfaces.service.GoogleRequestInitializer;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class SheetsClient {
    private static final String APPLICATION_NAME = "cashflow-438618";
    private final GoogleRequestInitializer googleRequestInitializer;

    public SheetsClient(GoogleRequestInitializer googleRequestInitializer) {
        this.googleRequestInitializer = googleRequestInitializer;
    }

    public Sheets getSheetsService() throws IOException, GeneralSecurityException {
        HttpRequestInitializer credential = this.googleRequestInitializer.authorize();

        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                new GsonFactory(),
                credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public List<List<Object>> getValueRange(String spreadSheetId, String range, RangeOptions rangeOptions) throws GeneralSecurityException, IOException {
        return getSheetsService().spreadsheets().values()
                .get(spreadSheetId, range)
                .setValueRenderOption(rangeOptions.getValueRenderOption())
                .setMajorDimension(rangeOptions.getMajorDimension())
                .execute()
                .getValues();
    }
}

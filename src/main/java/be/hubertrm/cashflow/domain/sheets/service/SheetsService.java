package be.hubertrm.cashflow.domain.sheets.service;

import be.hubertrm.cashflow.domain.sheets.entity.RangeOptions;
import be.hubertrm.cashflow.interfaces.client.SheetsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class SheetsService {

    private final SheetsClient sheetsClient;

    public SheetsService(SheetsClient sheetsClient) {
        this.sheetsClient = sheetsClient;
    }

    public List<List<Object>> getRangeBySpreadSheetIdRangeAndValueRenderOption(String spreadSheetId, String range, RangeOptions rangeOptions) {
        try {
            return this.sheetsClient.getValueRange(spreadSheetId, range, rangeOptions);
        } catch (GeneralSecurityException | IOException e) {
            log.debug("{} type exception triggered while trying to fetch range {} from sheet {}", e.getClass(), range, spreadSheetId, e);
            return Collections.emptyList();
        }
    }
}

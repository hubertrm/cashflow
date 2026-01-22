package be.hubertrm.cashflow.application.dto;

import lombok.Data;

import java.util.List;

@Data
public class SyncRequestDto {
    private String sheetId;
    private String range;
    private List<HeaderMapping> headers;

    @Data
    public static class HeaderMapping {
        private String header;
        private String headerType;
    }
}

package be.hubertrm.cashflow.application.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SyncResultDto {
    private int totalRows;
    private int successCount;
    private int skippedDuplicates;
    private int newAccountsCreated;
    private int newCategoriesCreated;
    private List<SyncErrorDto> errors = new ArrayList<>();

    @Data
    public static class SyncErrorDto {
        private int rowIndex;
        private String message;

        public SyncErrorDto(int rowIndex, String message) {
            this.rowIndex = rowIndex;
            this.message = message;
        }
    }
}

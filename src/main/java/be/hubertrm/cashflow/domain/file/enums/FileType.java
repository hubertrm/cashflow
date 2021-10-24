package be.hubertrm.cashflow.domain.file.enums;

public enum FileType {
    CSV("CSV_FILE_TYPE"), CSV_STRING("CSV_STRING_TYPE"),
    JSON("JSON_FILE_TYPE");

    private final String fileTypeId;

    FileType(String fileTypeId) {
        this.fileTypeId = fileTypeId;
    }

    public String getFileTypeId() {
        return fileTypeId;
    }
}

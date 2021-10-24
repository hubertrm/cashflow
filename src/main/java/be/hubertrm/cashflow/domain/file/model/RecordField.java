package be.hubertrm.cashflow.domain.file.model;

public enum RecordField {
    DATE("date"),
    PRICE("price"),
    CATEGORY("category"),
    ACCOUNT("account"),
    DESCRIPTION("description"),
    NOT_SUPPORTED("notSupported");

    private final String value;

    RecordField(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

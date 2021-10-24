package be.hubertrm.cashflow.domain.file.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Record {
    private String line;
    private List<String> fields;

    public Record(String line) {
        this.line = line;
        fields = new ArrayList<>();
    }

    public void add(String field) {
        fields.add(field);
    }
}

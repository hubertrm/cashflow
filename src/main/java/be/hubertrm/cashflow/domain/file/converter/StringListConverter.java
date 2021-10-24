package be.hubertrm.cashflow.domain.file.converter;

import be.hubertrm.cashflow.domain.file.converter.enums.OutputType;
import be.hubertrm.cashflow.domain.file.model.Record;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StringListConverter implements Converter<String, List<String>> {

    private static final OutputType outputType = OutputType.LIST;

    @Override
    public OutputType getSupportedOutputType() {
        return outputType;
    }

    @Override
    public List<String> convert(String line) {
        var rec = new Record(line);
        return splitThenFuse(rec).getFields();
    }

    @Override
    public Collection<List<String>> convert(Collection<String> lines) {
        return lines.stream().map(this::convert).collect(Collectors.toList());
    }

    public Record splitIteratively(Record rec) {
        while(!rec.getLine().isEmpty()) {
            splitUntilMarker(rec);
            takeUntilMarker(rec);
        }
        return rec;
    }

    public Record splitThenFuse(Record rec) {
        String line = rec.getLine();
        List<String> simpleSplit = Arrays.asList(line.split(DELIMITER));
        Iterator<String> iterator = simpleSplit.iterator();
        List<String> toBeFused = new ArrayList<>();
        var fusing = false;
        while (iterator.hasNext()) {
            String next = iterator.next();
            if (!fusing && this.isSurroundedByMarker(next)) {
                rec.add(next);
            } else if (next.startsWith(MARKER)) {
                fusing = true;
                next = next.substring(1);
                toBeFused.add(next);
            } else if (next.endsWith(MARKER)) {
                fusing = false;
                next = next.substring(0, next.length()-1);
                toBeFused.add(next);
                var fusedFields = String.join(DELIMITER, toBeFused);
                rec.add(fusedFields);
                toBeFused.clear();
            } else {
                rec.add(next);
            }
        }
        if (!toBeFused.isEmpty()) {
            rec.getFields().addAll(toBeFused);
        }
        return rec;
    }

    private void splitUntilMarker(Record rec) {
        String line = rec.getLine();
        var s = line.substring(0, line.indexOf(MARKER));
        rec.setLine(line.substring(line.indexOf(MARKER) + 1));
        rec.getFields().addAll(Arrays.stream(s.split(DELIMITER))
                .filter(sub -> !sub.isEmpty())
                .collect(Collectors.toList()));
    }

    private void takeUntilMarker(Record rec) {
        String line = rec.getLine();
        var s = line.substring(0, line.indexOf(MARKER));
        rec.setLine(line.substring(line.indexOf(MARKER) + 1));
        rec.getFields().addAll(Collections.singletonList(s));
    }

    private boolean isSurroundedByMarker(String field) {
        return field.startsWith(MARKER) && field.endsWith(MARKER);
    }
}

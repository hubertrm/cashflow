package be.hubertrm.cashflow.domain.file.converter;

import be.hubertrm.cashflow.domain.file.converter.enums.OutputType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StringArrayConverter implements Converter<String, ArrayList<String>> {

    private static final OutputType outputType = OutputType.ARRAY;

    @Override
    public OutputType getSupportedOutputType() {
        return outputType;
    }

    @Override
    public ArrayList<String> convert(String rec) {
        return split(rec);
    }

    @Override
    public Collection<ArrayList<String>> convert(Collection<String> recs) {
        return recs.stream().map(this::convert).collect(Collectors.toList());
    }

    private ArrayList<String> split(String rec) {
        ArrayList<String> results = new ArrayList<>();
        if (!StringUtils.hasText(DELIMITER) || !StringUtils.hasText(rec)) {
            results.add(rec);
            return results;
        }
        if (!rec.contains(MARKER)) {
            results.addAll(Arrays.stream(rec.split(DELIMITER)).collect(Collectors.toList()));
            return results;
        } else {
            String remaining = rec;
            while(StringUtils.hasText(remaining)) {
                String[] parts = splitToThree(remaining);
                results.addAll(Arrays.stream(parts[0].split(DELIMITER))
                        .filter(s -> !s.isBlank()).collect(Collectors.toList()));
                if(parts.length > 2) {
                    results.add(parts[1]);
                    remaining = parts[2];
                } else if(parts.length > 1) {
                    results.add(parts[1]);
                    remaining = "";
                } else {
                    remaining = "";
                }
            }
            return results;
        }
    }

    /**
     * Split a string of the form: "first-part{splitter}second-part{splitter}third-part"
     *      into an array :["first-part", "second-part", "third-part"]
     * @param value: the string to be split
     * @return an array of string
     */
    private String[] splitToThree(String value) {
        if(!value.contains(MARKER)) {
            return new String[]{value};
        }
        int first = value.indexOf(MARKER);
        int second = value.indexOf(MARKER, first+1);
        if(second < 0) {
            return new String[]{value};
        }
        return Stream.of(value.substring(0, first), value.substring(first+1, second), value.substring(second+1))
                .toArray(String[]::new);
    }
}

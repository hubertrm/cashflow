package be.hubertrm.cashflow.domain.file.reader;

import be.hubertrm.cashflow.domain.file.converter.Converter;
import be.hubertrm.cashflow.domain.file.converter.enums.OutputType;
import be.hubertrm.cashflow.domain.file.enums.FileType;
import be.hubertrm.cashflow.domain.file.factory.ConverterFactory;
import be.hubertrm.cashflow.domain.file.model.RecordEvaluated;
import be.hubertrm.cashflow.domain.file.service.EvaluatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CsvReader {

    @Resource
    private EvaluatorService service;
    @Resource
    private ConverterFactory converterFactory;

    private static final FileType fileType = FileType.CSV_STRING;

    public FileType getSupportedFileType() {
        return fileType;
    }

    public List<RecordEvaluated> read(String headers, String records) {
        return read(headers, records, Locale.ROOT);
    }

    public List<RecordEvaluated> read(String headers, String records, Locale locale) {
        List<RecordEvaluated> recordEvaluatedList = new ArrayList<>();
        String[] headerArray = getHeaders(headers);
        log.debug("First Line: {}", Arrays.toString(headerArray));
        extractFromStringToList(records, recordEvaluatedList, headerArray, locale);
        recordEvaluatedList.forEach(recordEvaluated -> log.info("Record {}", recordEvaluated));
        return recordEvaluatedList;
    }

    private String[] getHeaders(String content) {
        Converter<String, ? extends List<String>> converter = converterFactory.createConverter(OutputType.ARRAY);
        return converter.convert(content.toLowerCase().trim()).toArray(new String[]{});
    }

    private void extractFromStringToList(String content, List<RecordEvaluated> recordDtoList, String[] headers, Locale locale) {
        recordDtoList.addAll(Arrays.stream(content.split("\n"))
            .filter(this::filterLine)
            .map(line -> service.create(headers, line, locale))
            .collect(Collectors.toList()));
    }

    private boolean filterLine(String line) {
        return Objects.nonNull(line) && !line.isEmpty();
    }
}

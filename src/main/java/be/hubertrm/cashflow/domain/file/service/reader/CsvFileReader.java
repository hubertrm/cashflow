package be.hubertrm.cashflow.domain.file.service.reader;

import be.hubertrm.cashflow.domain.file.service.converter.Converter;
import be.hubertrm.cashflow.domain.file.service.converter.enums.OutputType;
import be.hubertrm.cashflow.domain.file.enums.FileType;
import be.hubertrm.cashflow.domain.file.exception.FieldsNotFoundException;
import be.hubertrm.cashflow.domain.file.exception.FileNotFoundException;
import be.hubertrm.cashflow.domain.file.service.factory.ConverterFactory;
import be.hubertrm.cashflow.domain.file.model.RecordEvaluated;
import be.hubertrm.cashflow.domain.file.service.EvaluatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class CsvFileReader implements FileReader {

    @Resource
    private EvaluatorService service;
    @Resource
    private ConverterFactory converterFactory;

    private static final FileType fileType = FileType.CSV;

    @Override
    public FileType getSupportedFileType() {
        return fileType;
    }

    public List<RecordEvaluated> read(String filename) throws FileNotFoundException {
        return read(Paths.get(filename));
    }

    public List<RecordEvaluated> read(String filename, Locale locale) throws FileNotFoundException {
        return read(Paths.get(filename), locale);
    }

    public List<RecordEvaluated> read(Path path) throws FileNotFoundException {
        return read(path, Locale.ROOT);
    }

    public List<RecordEvaluated> read(Path path, Locale locale) throws FileNotFoundException {
        if (!Files.exists(path)) {
            throw new FileNotFoundException(String.format("File %s does not exist.", path));
        }
        List<RecordEvaluated> recordEvaluatedList = new ArrayList<>();
        try {
            String[] headers = getHeaders(path);
            log.debug("First Line: {}", Arrays.toString(headers));
            extractFromFileToList(path, recordEvaluatedList, headers, locale);
        } catch (FieldsNotFoundException | IOException e) {
            log.error("Error reading file from path {}", path, e);
        }
        recordEvaluatedList.forEach(recordDto -> log.info("Record {}", recordDto));
        return recordEvaluatedList;
    }

    private String[] getHeaders(Path path) throws FieldsNotFoundException, IOException {
        Converter<String, ? extends List<String>> converter = converterFactory.createConverter(OutputType.ARRAY);
        try (Stream<String> lines = Files.lines(path)) {
            Optional<String> firstLine = lines.findFirst();
            return firstLine.map(s -> converter.convert(s.toLowerCase()))
                    .orElseThrow(() -> new FieldsNotFoundException("Field Not found")).toArray(new String[]{});
        }
    }

    private void extractFromFileToList(Path path, List<RecordEvaluated> recordDtoList, String[] headers, Locale locale)
            throws IOException {
        try (Stream<String> fileContentStream = Files.lines(path)) {
            recordDtoList.addAll(fileContentStream
                .skip(1)
                .filter(this::filterLine)
                .map(line -> service.create(headers, line, locale))
                .collect(Collectors.toList()));
        }
    }

    private boolean filterLine(String line) {
        return Objects.nonNull(line) && !line.isEmpty();
    }
}

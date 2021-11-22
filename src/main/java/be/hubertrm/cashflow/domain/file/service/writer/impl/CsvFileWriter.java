package be.hubertrm.cashflow.domain.file.service.writer.impl;

import be.hubertrm.cashflow.domain.file.service.writer.FileWriter;
import be.hubertrm.cashflow.application.dto.TransactionDto;
import be.hubertrm.cashflow.domain.file.enums.FileType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CsvFileWriter implements FileWriter {

    private static final String DELIMITER = ",";
    private static final FileType fileType = FileType.CSV;
    private static final StandardOpenOption[] options = {StandardOpenOption.CREATE,
            StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING};

    @Override
    public FileType getSupportedFileType() {
        return fileType;
    }

    @Override
    public List<TransactionDto> write(String filename, List<TransactionDto> list) throws IOException {
        var path = Paths.get(filename);
        List<TransactionDto> writtenLines = new ArrayList<>();
        try (var writer = Files.newBufferedWriter(path, options)) {
            for (var transactionDto: list) {
                writeRecordDto(filename, writtenLines, writer, transactionDto);
            }
        } catch (IOException e) {
            log.debug("Could not write to file {}", filename, e);
            throw new IOException("Could not write to file " + filename, e);
        }
        if (writtenLines.isEmpty()) {
            log.info("No records have been successfully saved");
        } else {
            log.info("{} out of {} records have been successfully saved", writtenLines.size(), list.size());
        }
        return writtenLines;
    }

    private void writeRecordDto(String filename, List<TransactionDto> writtenLines, BufferedWriter writer, TransactionDto transactionDto) {
        try {
            writer.write(convert(transactionDto));
            writtenLines.add(transactionDto);
            writer.newLine();
        } catch (Exception e) {
            log.debug("Error while writing element {} to file {}", transactionDto, filename, e);
        }
    }

    private String convert(TransactionDto transactionDto) {
        return String.join(DELIMITER,
                normalize(transactionDto.getDate().toString()),
                normalize(Float.toString(transactionDto.getAmount())),
                normalize(transactionDto.getCategory().getName()),
                normalize(transactionDto.getAccount().getName()),
                normalize(transactionDto.getDescription()));
    }

    private String normalize(String value) {
        return value.trim();
    }
}

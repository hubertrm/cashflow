package be.hubertrm.cashflow.domain.file.factory;

import be.hubertrm.cashflow.domain.file.enums.FileType;
import be.hubertrm.cashflow.domain.file.exception.ReadWriteException;
import be.hubertrm.cashflow.domain.file.reader.CsvFileReader;
import be.hubertrm.cashflow.domain.file.reader.FileReader;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class FileReaderFactoryTest {
    private FileReader expectedFileReader;
    private FileReaderFactory fileReaderFactory;

    @BeforeEach
    void setup() {
        expectedFileReader = new CsvFileReader();
        Set<FileReader> writerSet = Set.of(expectedFileReader);
        fileReaderFactory = new FileReaderFactory(writerSet);
    }

    @Test
    void createCsvFileReader() throws ReadWriteException {
        Assertions.assertThat(fileReaderFactory.create(FileType.CSV)).isEqualTo(expectedFileReader);
    }

    @Test
    void nonSupportedFileType_shouldThrowException() {
        assertThatThrownBy(() -> fileReaderFactory.create(FileType.JSON)).isInstanceOf(ReadWriteException.class);
    }

}
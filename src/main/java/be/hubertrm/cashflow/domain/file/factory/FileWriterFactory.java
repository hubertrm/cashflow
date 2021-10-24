package be.hubertrm.cashflow.domain.file.factory;

import be.hubertrm.cashflow.domain.file.enums.FileType;
import be.hubertrm.cashflow.domain.file.exception.ReadWriteException;
import be.hubertrm.cashflow.domain.file.writer.FileWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@code WriterManager} class represents a factory creator of FileWriter objects
 * in the record Management System.
 * <br>
 *    The WriterManager can create {@link FileWriter} objects based on
 *    <ul>
 *        <li>a {@link FileType}</li>
 *    </ul>
 * <br>
 * <p>ON : may 22, 2021
 *
 * @version 1.0
 * @author Hubert Romain - hubertrm
 */
@Slf4j
@Service
public class FileWriterFactory {

    private Map<FileType, FileWriter> writerServices;

    @Autowired
    public FileWriterFactory(Set<FileWriter> writerSet) {
        populateWriterServices(writerSet);
    }

    public FileWriter create(FileType type) throws ReadWriteException {
        if (!writerServices.containsKey(type)) {
            throw new ReadWriteException("Type not supported yet");
        }
        return writerServices.get(type);
    }

    private void populateWriterServices(Set<FileWriter> writerSet) {
        writerServices = new EnumMap<>(writerSet.stream()
                .collect(Collectors.toMap(FileWriter::getSupportedFileType, fileWrite -> fileWrite)));
    }
}

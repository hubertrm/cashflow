package be.hubertrm.cashflow.domain.file.factory;

import be.hubertrm.cashflow.domain.file.enums.FileType;
import be.hubertrm.cashflow.domain.file.exception.ReadWriteException;
import be.hubertrm.cashflow.domain.file.reader.FileReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@code FileReaderFactory} class represents a factory creator of FileReader objects
 * in the record Management System.
 * <br>
 *    The FileReaderFactory can create {@link FileReader} objects based on
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
public class FileReaderFactory {

    private Map<FileType, FileReader> readerServices;

    @Autowired
    public FileReaderFactory(Set<FileReader> readerSet) {
        populateReaderServices(readerSet);
    }

    public FileReader create(FileType type) throws ReadWriteException {
        if (!readerServices.containsKey(type)) {
            throw new ReadWriteException("Type not supported yet");
        }
        return readerServices.get(type);
    }

    private void populateReaderServices(Set<FileReader> readerSet) {
        readerServices = new EnumMap<>(readerSet.stream()
                .collect(Collectors.toMap(FileReader::getSupportedFileType, fileReader -> fileReader)));
    }

}

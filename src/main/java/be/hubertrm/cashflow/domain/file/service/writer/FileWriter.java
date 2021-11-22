package be.hubertrm.cashflow.domain.file.service.writer;

import be.hubertrm.cashflow.application.dto.TransactionDto;

import java.io.IOException;
import java.util.List;

/**
 * {@code FileWriter} class represents properties and behaviors of FileWriter objects
 * in the record Management System.
 * <br>
 *    The FileWriter can write a collection of {@link TransactionDto} objects to a file.
 * <br>
 * <p>ON : may 22, 2021
 *
 * @version 1.0
 * @author Hubert Romain - hubertrm
 */
public interface FileWriter extends Writer {

    /**
     * Write a collection of {@link TransactionDto}s to a given file identified by its filename.
     * If the file does not exists, it is created. Otherwise it is replaced.
     *
     * @param filename: the name of the file in which the records need to be written
     * @param list: the List of {@link TransactionDto} objects to be written out
     * @return the List of {@link TransactionDto} objects that have not been written to the file
     */
    List<TransactionDto> write(String filename, List<TransactionDto> list) throws IOException;
}

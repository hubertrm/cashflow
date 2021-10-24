package be.hubertrm.cashflow.facade.file.writer;

import be.hubertrm.cashflow.facade.dto.TransactionDto;
import be.hubertrm.cashflow.facade.file.enums.FileType;

/**
 * {@code Writer} class represents properties and behaviors of Writer objects
 * in the record Management System.
 * <br>
 *    The Writer can write a collection of {@link TransactionDto} objects to a destination (File, Database).
 * <br>
 * <p>ON : may 22, 2021
 *
 * @version 1.0
 * @author Hubert Romain - hubertrm
 */
public interface Writer {

    /**
     * @return the class unique supported {@link FileType}
     */
    FileType getSupportedFileType();
}

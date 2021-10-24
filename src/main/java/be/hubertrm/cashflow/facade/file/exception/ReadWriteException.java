package be.hubertrm.cashflow.facade.file.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <p>ON : may. 22, 2021
 *
 * @author Hubert Romain - hubertrm
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ReadWriteException extends Exception {

	public ReadWriteException(String message) {
		super(message);
	}

}

package be.hubertrm.cashflow.facade.file.exception;

public class FileNotFoundException extends Exception {

    private static final long serialVersionUID = -1L;

    public FileNotFoundException(String message) {
        super(message);
    }
}

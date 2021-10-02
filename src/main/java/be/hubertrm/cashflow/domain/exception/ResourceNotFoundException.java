package be.hubertrm.cashflow.domain.exception;

public class ResourceNotFoundException extends Exception {

    private static final long serialVersionUID = -1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Object ...args) {
        super(String.format(message, args));
    }
}

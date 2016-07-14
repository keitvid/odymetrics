package dbService;

/**
 * Created by IPermyakova on 12.07.2016.
 */
public class ColumnException extends Exception {

    public ColumnException(String message) {
        super(message);
    }

    public ColumnException(String message, Throwable cause) {
        super(message, cause);
    }
}

package starj.util.text;

public class TextWidthOverflowException extends RuntimeException {
    public TextWidthOverflowException() {
        super();
    }

    public TextWidthOverflowException(String message) {
        super(message);
    }

    public TextWidthOverflowException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public TextWidthOverflowException(Throwable cause) {
        super(cause);
    }
}


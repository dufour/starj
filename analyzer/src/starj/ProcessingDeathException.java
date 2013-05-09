package starj;

/**
 * Thrown when processing of a *J trace file fails.
 * 
 * @author Bruno Dufour
 */
public class ProcessingDeathException extends Exception {
    /**
     * Creates a new <code>ProcessingDeathException</code> with
     * <code>null</code> as its detail message.
     */
    public ProcessingDeathException() {
        super();
    }
    
    /**
     * Creates a new <code>ProcessingDeathException</code> with the specified
     * detail message.
     * @param message The detail message.
     */
    public ProcessingDeathException(String message) {
        super(message);
    }
    
    /**
     * Creates a new <code>ProcessingDeathException</code> with the specified
     * detail message and cause.
     * @param message The detail message.
     * @param cause The <code>Throwable</code> instance that caused the
     *     processing to fail.
     */
    public ProcessingDeathException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Creates a new <code>ProcessingDeathException</code> with the specified
     * cause and a detail message of
     * <code>(cause==null ? null : cause.toString())</code>.
     * @param message The detail message.
     * @param cause The <code>Throwable</code> instance that caused the
     *     processing to fail.
     */
    public ProcessingDeathException(Throwable cause) {
        super(cause);
    }
}

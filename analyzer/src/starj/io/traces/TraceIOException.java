package starj.io.traces;

/**
 * Thrown when an problem is encountered while parsing a *J trace file.
 * 
 * @author Bruno Dufour
 */
public class TraceIOException extends Exception {
    /**
     * Constructs a <code>TraceIOException</code> with no detail message.
     */
    public TraceIOException() {
        super();
    }

    /**
     * Constructs a <code>TraceIOException</code> with the
     * specified detail message.
     *
     * @param message the detail message
     */
    public TraceIOException(String message) {
        super(message);
    }
}

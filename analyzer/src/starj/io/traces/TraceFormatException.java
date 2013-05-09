package starj.io.traces;

/**
 * Thrown when parsing a *J trace file which has an invalid format.
 * 
 * @author Bruno Dufour
 */
public class TraceFormatException extends TraceIOException {
    /**
     * Constructs a <code>TraceFormatException</code> with no detail message.
     */
    public TraceFormatException() {
        super();
    }

    /**
     * Constructs a <code>TraceFormatException</code> with the
     * specified detail message.
     *
     * @param message the detail message
     */
    public TraceFormatException(String message) {
        super(message);
    }
}

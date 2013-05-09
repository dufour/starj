package starj;

/**
 * An exception which is thrown by an {@link starj.Operation Operation} 
 * in order to cause the event processing loop to immediately skip
 * to the next event, thus preventing subsequent operations to receive
 * the current event.
 *
 * @author Bruno Dufour
 */
public class EventSkipException extends RuntimeException {
    public EventSkipException() {
        super();
    }
}

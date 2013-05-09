package starj.events;

/**
 * An Event corresponding to the <code>JVMPI_METHOD_ENTRY</code> event. This
 * event is triggered when a method is entered (i.e.  its code starts being
 * executed). <code>MethodEntryEvent</code> is similar to
 * <code>MethodEntry2Event</code> except that <code>MethodEntry2Event</code>
 * additionally provides the object ID of the target in the case of an
 * <code>invokevirtual</code>.
 *
 * @author Bruno Dufour
 * @see MethodEntry2Event
 * @see MethodExitEvent
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class MethodEntryEvent extends AbstractMethodEntryEvent
        implements MethodEvent {
    public MethodEntryEvent() {
        super(Event.METHOD_ENTRY);
    }
    
    public MethodEntryEvent(int env_id, int method_id) {
        this(env_id, method_id, false);
    }

    public MethodEntryEvent(int env_id, int method_id, boolean requested) {
        super(Event.METHOD_ENTRY, env_id, method_id, requested);
    }
}

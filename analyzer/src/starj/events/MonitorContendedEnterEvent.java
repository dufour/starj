package starj.events;

/**
 * An Event corresponding to the <code>JVMPI_MONITOR_CONTENDED_ENTER</code>
 * event. This event is triggered when a thread is attempting to enter a Java
 * monitor already acquired by another thread.
 *
 * @author Bruno Dufour
 * @see MonitorContendedEnteredEvent
 * @see MonitorContendedExitEvent
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class MonitorContendedEnterEvent extends MonitorEvent {
    public MonitorContendedEnterEvent() {
        super(Event.MONITOR_CONTENDED_ENTER);
    }

    public MonitorContendedEnterEvent(int env_id, int object) {
        this(env_id, object, false);
    }

    public MonitorContendedEnterEvent(int env_id, int object,
            boolean requested) {
        super(Event.MONITOR_CONTENDED_ENTER, env_id, object, requested);
    }
}

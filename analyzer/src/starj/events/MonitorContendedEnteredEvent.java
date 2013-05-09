package starj.events;

/**
 * An Event corresponding to the <code>JVMPI_MONITOR_CONTENDED_ENTERED</code>
 * event. This event is triggered when a thread successfully enters a Java
 * monitor after having waited for it to be released by another thread.
 *
 * @author Bruno Dufour
 * @see MonitorContendedEnterEvent
 * @see MonitorContendedExitEvent
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class MonitorContendedEnteredEvent extends MonitorEvent {
    public MonitorContendedEnteredEvent() {
        super(Event.MONITOR_CONTENDED_ENTERED);
    }

    public MonitorContendedEnteredEvent(int env_id, int object) {
        this(env_id, object, false);
    }

    public MonitorContendedEnteredEvent(int env_id, int object,
            boolean requested) {
        super(Event.MONITOR_CONTENDED_ENTERED, env_id, object, requested);
    }
}

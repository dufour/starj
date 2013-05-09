package starj.events;

/**
 * An Event corresponding to the <code>JVMPI_MONITOR_CONTENDED_EXIT</code>
 * event. This event is triggered when a thread exits a Java monitor, and
 * another thread is waiting to acquire the same monitor.
 *
 * @author Bruno Dufour
 * @see MonitorContendedEnterEvent
 * @see MonitorContendedEnteredEvent
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class MonitorContendedExitEvent extends MonitorEvent {
    public MonitorContendedExitEvent() {
        super(Event.MONITOR_CONTENDED_EXIT);
    }

    public MonitorContendedExitEvent(int env_id, int object) {
        this(env_id, object, false);
    }

    public MonitorContendedExitEvent(int env_id, int object,
            boolean requested) {
        super(Event.MONITOR_CONTENDED_EXIT, env_id, object, requested);
    }
}

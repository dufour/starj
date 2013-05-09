package starj.events;

/**
 * An Event corresponding to the <code>JVMPI_MONITOR_WAITED</code> event. This
 * event is triggered when a thread finishes waiting on a object.
 *
 * @author Bruno Dufour
 * @see MonitorWaitEvent
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class MonitorWaitedEvent extends TimedMonitorEvent {
    public MonitorWaitedEvent() {
        super(Event.MONITOR_WAITED);
    }

    public MonitorWaitedEvent(int env_id, int object, long timeout) {
        this(env_id, object, timeout, false);
    }

    public MonitorWaitedEvent(int env_id, int object, long timeout,
            boolean requested) {
        super(Event.MONITOR_WAITED, env_id, object, timeout, requested);
    }
}

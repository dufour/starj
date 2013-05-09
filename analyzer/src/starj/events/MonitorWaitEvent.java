package starj.events;

/**
 * An Event corresponding to the <code>JVMPI_MONITOR_WAIT</code> event. This
 * event is triggered when a thread is about to wait on a object.
 *
 * @author Bruno Dufour
 * @see MonitorWaitedEvent
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class MonitorWaitEvent extends TimedMonitorEvent {
    public MonitorWaitEvent() {
        super(Event.MONITOR_WAIT);
    }

    public MonitorWaitEvent(int env_id, int object, long timeout) {
        this(env_id, object, timeout, false);
    }

    public MonitorWaitEvent(int env_id, int object, long timeout,
            boolean requested) {
        super(Event.MONITOR_WAIT, env_id, object, timeout, requested);
    }
}

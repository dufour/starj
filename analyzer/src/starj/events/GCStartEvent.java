package starj.events;

/**
 * An Event corresponding to the <code>JVMPI_GC_START</code> event. This event
 * is triggered after the Garbage Collector (GC) of the Java VM starts to run.
 *
 * @author Bruno Dufour
 * @see GCFinishEvent
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class GCStartEvent extends Event implements GCEvent {
    public GCStartEvent() {
        super(Event.GC_START);
    }

    public GCStartEvent(int env_id) {
        this(env_id, false);
    }

    public GCStartEvent(int env_id, boolean requested) {
        super(Event.GC_START, env_id, requested);
    }
}

package starj.events;

/**
 * An Event corresponding to the <code>JVMPI_THREAD_END</code> event. This
 * event is triggered when a thread ends in the Java VM.
 *
 * @author Bruno Dufour
 * @see ThreadStartEvent
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class ThreadEndEvent extends Event implements ThreadEvent {
    public ThreadEndEvent() {
        super(Event.THREAD_END);
    }

    public ThreadEndEvent(int env_id) {
        this(env_id, false);
    }

    public ThreadEndEvent(int env_id, boolean requested) {
        super(Event.THREAD_END, env_id, requested);
    }
}

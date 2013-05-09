package starj.events;

/**
 * An Event corresponding to the <code>JVMPI_JVM_INIT_DONE</code> event. This
 * event is triggered when the initialization phase of the Java VM is
 * completed.
 *
 * @author Bruno Dufour
 * @see JVMShutDownEvent
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class JVMInitDoneEvent extends Event implements JVMEvent {
    public JVMInitDoneEvent() {
        super(Event.JVM_INIT_DONE);
    }

    public JVMInitDoneEvent(int env_id) {
        this(env_id, false);
    }

    public JVMInitDoneEvent(int env_id, boolean requested) {
        super(Event.JVM_INIT_DONE, env_id, requested);
    }
}

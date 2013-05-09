package starj.events;

/**
 * An Event corresponding to the <code>JVMPI_JVM_SHUT_DOWN</code> event. This
 * event is triggered when the Java VM is done executing the application.
 *
 * @author Bruno Dufour
 * @see JVMShutDownEvent
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class JVMShutDownEvent extends Event implements JVMEvent {
    public JVMShutDownEvent() {
        super(Event.JVM_SHUT_DOWN);
    }

    public JVMShutDownEvent(int env_id) {
        this(env_id, false);
    }

    public JVMShutDownEvent(int env_id, boolean requested) {
        super(Event.JVM_SHUT_DOWN, env_id, requested);
    }
}

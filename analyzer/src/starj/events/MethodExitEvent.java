package starj.events;

import java.io.IOException;

import starj.io.traces.TraceInput;
import starj.spec.Constants;

/**
 * An Event corresponding to the <code>JVMPI_METHOD_EXIT</code> event. This
 * event is triggered when a method is exited (i.e. its code has finished
 * executing).
 *
 * @author Bruno Dufour
 * @see MethodEntryEvent
 * @see MethodEntry2Event
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class MethodExitEvent extends Event implements MethodEvent {
    private int method_id;

    public MethodExitEvent() {
        super(Event.METHOD_EXIT);
        this.reset();
    }
    
    public MethodExitEvent(int env_id, int method_id) {
        this(env_id, method_id, false);
    }

    public MethodExitEvent(int env_id, int method_id, boolean requested) {
        super(Event.METHOD_EXIT, env_id, requested);
        this.method_id = method_id;
    }

    public int getMethodID() {
        return this.method_id;
    }
    
    public void setMethodID(int method_id) {
        this.method_id = method_id;
    }

    public void readFromStream(TraceInput in, int mask, boolean requested)
            throws IOException {
        super.readFromStream(in, mask, requested);
        
        if ((mask & Constants.FIELD_METHOD_ID) != 0) {
            this.method_id = in.readMethodID();
        }
    }

    public void reset() {
        super.reset();

        this.method_id = 0;
    }
}

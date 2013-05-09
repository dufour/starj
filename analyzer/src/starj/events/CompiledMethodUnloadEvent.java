package starj.events;

import java.io.IOException;

import starj.spec.Constants;
import starj.io.traces.TraceInput;

/**
 * An Event corresponding to the <code>JVMPI_COMPILED_METHOD_UNLOAD</code>
 * event. This event is triggered when a compiled method is unloaded from
 * memory by the Java VM.
 *
 * @author Bruno Dufour
 * @see CompiledMethodLoadEvent
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class CompiledMethodUnloadEvent extends Event implements MethodEvent {
    private int method_id;

    public CompiledMethodUnloadEvent() {
        super(Event.COMPILED_METHOD_UNLOAD);
        this.reset();
    }
    
    public CompiledMethodUnloadEvent(int env_id, int method_id) {
        this(env_id, method_id, false);
    }

    public CompiledMethodUnloadEvent(int env_id, int method_id,
            boolean requested) {
        super(Event.COMPILED_METHOD_UNLOAD, env_id, requested);
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

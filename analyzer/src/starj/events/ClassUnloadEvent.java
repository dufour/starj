package starj.events;

import java.io.IOException;

import starj.spec.Constants;
import starj.io.traces.TraceInput;

/**
 * An Event corresponding to the <code>JVMPI_CLASS_UNLOAD</code> event. This
 * event is triggered when a previously loaded class is unloaded by the Java
 * VM.
 *
 * @author Bruno Dufour
 * @see ClassLoadEvent
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class ClassUnloadEvent extends Event implements ClassEvent {
    /** The ID of the class being unloaded. */
    private int class_id;

    public ClassUnloadEvent() {
        super(Event.CLASS_UNLOAD);
        this.reset();
    }

    public ClassUnloadEvent(int env_id, int class_id) {
        this(env_id, class_id, false);
    }

    public ClassUnloadEvent(int env_id, int class_id, boolean requested) {
        super(Event.CLASS_UNLOAD, env_id, requested);
        this.class_id = class_id;
    }
    
    public int getClassID() {
        return this.class_id;
    }
     
    public void setClassID(int class_id) {
        this.class_id = class_id;
    }

    public void readFromStream(TraceInput in, int mask, boolean requested)
            throws IOException {
        super.readFromStream(in, mask, requested);

        if ((mask & Constants.FIELD_CLASS_UNLOAD_CLASS_ID) != 0) {
            this.class_id = in.readClassID();
        }
    }

    public void reset() {
        super.reset();

        this.class_id = 0;
    }
}

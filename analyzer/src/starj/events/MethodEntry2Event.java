package starj.events;

import java.io.IOException;

import starj.io.traces.TraceInput;
import starj.spec.Constants;

/**
 * An Event corresponding to the <code>JVMPI_METHOD_ENTRY2</code> event. This
 * event is triggered when a method is entered (i.e.  its code starts being
 * executed). <code>MethodEntry2Event</code> is similar to
 * <code>MethodEntryEvent</code> except that it additionally provides the
 * object ID of the target in the case of an <code>invokevirtual</code>.
 *
 * @author Bruno Dufour
 * @see MethodEntryEvent
 * @see MethodExitEvent
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class MethodEntry2Event extends AbstractMethodEntryEvent
        implements MethodEvent, ObjectEvent {
    /**
     * The ID of object which is the target of the method invocation which
     * caused this event to be triggered. The value is 0 for static methods.
     */
    private int obj_id;

    public MethodEntry2Event() {
        super(Event.METHOD_ENTRY2);
    }
    
    public MethodEntry2Event(int env_id, int method_id, int obj_id) {
        this(env_id, method_id, obj_id, false);
    }

    public MethodEntry2Event(int env_id, int method_id, int obj_id,
            boolean requested) {
        super(Event.METHOD_ENTRY2, env_id, method_id, requested);
        this.obj_id = obj_id;
    }

    public int getObjectID() {
        return this.obj_id;
    }
    
    public void setObjectID(int obj_id) {
        this.obj_id = obj_id;
    }

    public void readFromStream(TraceInput in, int mask, boolean requested)
            throws IOException {
        super.readFromStream(in, mask, requested);

        if ((mask & Constants.FIELD_OBJ_ID) != 0) {
            this.obj_id = in.readObjectID();
        }
    }

    public void reset() {
        super.reset();

        this.obj_id = 0;
    }
}

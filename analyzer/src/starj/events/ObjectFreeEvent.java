package starj.events;

import java.io.IOException;

import starj.io.traces.TraceInput;
import starj.spec.Constants;

/**
 * An Event corresponding to the <code>JVMPI_OBJECT_FREE</code> event. This
 * event is triggered when an object is freed by the Java VM.
 *
 * @author Bruno Dufour
 * @see ObjectAllocEvent
 * @see ObjectMoveEvent
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class ObjectFreeEvent extends Event implements ObjectEvent {
    /** The ID of the freed object. */
    private int obj_id;

    public ObjectFreeEvent() {
        super(Event.OBJECT_FREE);
        this.reset();
    }

    public ObjectFreeEvent(int env_id, int obj_id) {
        this(env_id, obj_id, false);
    }
    public ObjectFreeEvent(int env_id, int obj_id, boolean requested) {
        super(Event.OBJECT_FREE, env_id, requested);
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

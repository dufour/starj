package starj.events;

import java.io.IOException;

import starj.io.traces.TraceInput;
import starj.spec.Constants;

/**
 * An Event corresponding to the <code>JVMPI_GC_FINISH</code> event. This event
 * is triggered after the Garbage Collector (GC) of the Java VM has run.
 *
 * @author Bruno Dufour
 * @see GCStartEvent
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class GCFinishEvent extends Event implements GCEvent {
    /** The number of used objects in the heap. */   
    private long used_objects;
    /** The total amount of space (in bytes) used by the objects in the heap. */
    private long used_object_space;
    /** The total amount of object space (in bytes) available in the heap. */   
    private long total_object_space;
    
    public GCFinishEvent() {
        super(Event.GC_FINISH);
        this.reset();
    }
    
    public GCFinishEvent(int env_id, long used_objects, long used_object_space,
            long total_object_space) {
        this(env_id, used_objects, used_object_space, total_object_space,
                false);
    }

    public GCFinishEvent(int env_id, long used_objects, long used_object_space,
            long total_object_space, boolean requested) {
        super(Event.GC_FINISH, env_id, requested);
        this.used_objects = used_objects;
        this.used_object_space = used_object_space;
        this.total_object_space = total_object_space;
    }
    
    public long getUsedObjects() {
        return this.used_objects;
    }
    
    public void setUsedObjects(long used_objects) {
        this.used_objects = used_objects;
    }
    
    public long getUsedObjectSpace() {
        return this.used_object_space;
    }
    
    public void setUsedObjectSpace(long used_object_space) {
        this.used_object_space = used_object_space;
    }
    
    public long getTotalObjectSpace() {
        return this.total_object_space;
    }
    
    public void setTotalObjectSpace(long total_object_space) {
        this.total_object_space = total_object_space;
    }
    
    public void readFromStream(TraceInput in, int mask, boolean requested)
            throws IOException {
        super.readFromStream(in, mask, requested);

        if ((mask & Constants.FIELD_USED_OBJECTS) != 0) {
            this.used_objects = in.readLong();
        }
        if ((mask & Constants.FIELD_USED_OBJECT_SPACE) != 0) {
            this.used_object_space = in.readLong();
        }
        if ((mask & Constants.FIELD_TOTAL_OBJECT_SPACE) != 0) {
            this.total_object_space = in.readLong();
        }
    }

    public void reset() {
        super.reset();

        this.used_objects = -1L;
        this.used_object_space = -1L;
        this.total_object_space = -1L;
    }
}

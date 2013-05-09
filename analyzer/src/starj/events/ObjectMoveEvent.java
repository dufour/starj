package starj.events;

import java.io.IOException;

import starj.io.traces.TraceInput;
import starj.spec.Constants;

/**
 * An Event corresponding to the <code>JVMPI_OBJECT_MOVE</code> event. This
 * event is triggered when an object is moved in the heap by the Java VM.
 *
 * @author Bruno Dufour
 * @see ObjectAllocEvent
 * @see ObjectFreeEvent
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class ObjectMoveEvent extends Event implements ArenaEvent, ObjectEvent {
    /** The ID of the arena in which the object can be found. */
    private int arena_id;
    /** The ID of the object being moved. */
    private int obj_id;
    
    /** The ID of the arena to which the object is being moved. */
    private int new_arena_id;

    /** The new ID of the object. */
    private int new_obj_id;

    public ObjectMoveEvent() {
        super(Event.OBJECT_MOVE);
        this.reset();
    }

    public ObjectMoveEvent(int env_id, int arena_id, int obj_id,
            int new_arena_id, int new_obj_id) {
        this(env_id, arena_id, obj_id, new_arena_id, new_obj_id, false);
    }

    public ObjectMoveEvent(int env_id, int arena_id, int obj_id,
            int new_arena_id, int new_obj_id, boolean requested) {
        super(Event.OBJECT_MOVE, env_id, requested);
        this.arena_id = arena_id;
        this.obj_id = obj_id;
        this.new_arena_id = new_arena_id;
        this.new_obj_id = new_obj_id;
    }
    
    public int getArenaID() {
        return this.arena_id;
    }
    
    public void setArenaID(int arena_id) {
        this.arena_id = arena_id;
    }

    public int getObjectID() {
        return this.obj_id;
    }
    
    public void setObjectID(int obj_id) {
        this.obj_id = obj_id;
    }

    public int getNewArenaID() {
        return this.new_arena_id;
    }
    
    public void setNewArenaID(int new_arena_id) {
        this.new_arena_id = new_arena_id;
    }

    public int getNewObjectID() {
        return this.new_obj_id;
    }
    
    public void setNewObjectID(int new_obj_id) {
        this.new_obj_id = new_obj_id;
    }

    public void readFromStream(TraceInput in, int mask, boolean requested)
            throws IOException {
        super.readFromStream(in, mask, requested);

        if ((mask & Constants.FIELD_ARENA_ID) != 0) {
            this.arena_id = in.readArenaID();
        }
        
        if ((mask & Constants.FIELD_OBJ_ID) != 0) {
            this.obj_id = in.readObjectID();
        }
        
        if ((mask & Constants.FIELD_NEW_ARENA_ID) != 0) {
            this.new_arena_id = in.readArenaID();
        }
        
        if ((mask & Constants.FIELD_NEW_OBJ_ID) != 0) {
            this.new_obj_id = in.readObjectID();
        }
    }

    public void reset() {
        super.reset();

        this.arena_id = 0;
        this.new_arena_id = 0;
        this.obj_id = 0;
        this.new_obj_id = 0;
    }
}

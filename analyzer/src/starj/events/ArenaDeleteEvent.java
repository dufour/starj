package starj.events;

import java.io.IOException;

import starj.spec.Constants;
import starj.io.traces.TraceInput;

/**
 * An event corresponding to the <code>JVMPI_ARENA_DELETE</code> event. This
 * event is triggered when a heap arena is deleted. It results in the deletion
 * of all bjects in this arena.
 *
 * @author Bruno Dufour
 * @see ArenaNewEvent
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 * Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class ArenaDeleteEvent extends Event implements ArenaEvent {
    /** The ID of the arena being deleted. */
    private int arena_id;

    public ArenaDeleteEvent() {
        super(Event.ARENA_DELETE);
        this.reset();
    }
    
    public ArenaDeleteEvent(int env_id, int arena_id) {
        this(env_id, arena_id, false);
    }
    
    public ArenaDeleteEvent(int env_id, int arena_id, boolean requested) {
        super(Event.ARENA_DELETE, env_id, requested);
        this.arena_id = arena_id;
    }

    public int getArenaID() {
        return this.arena_id;
    }
    
    public void setArenaID(int arena_id) {
        this.arena_id = arena_id;
    }

    public void readFromStream(TraceInput in, int mask, boolean requested)
            throws IOException {
        super.readFromStream(in, mask, requested);

        if ((mask & Constants.FIELD_ARENA_ID) != 0) {
            this.arena_id = in.readArenaID();
        }
    }

    public void reset() {
        super.reset();

        this.arena_id = 0;
    }
}

package starj.events;

import java.io.IOException;

import starj.spec.Constants;
import starj.io.traces.TraceInput;

/**
 * An Event corresponding to the <code>JVMPI_ARENA_NEW</code> event. This event
 * is triggered when a heap arena is created by the Java VM.
 *
 * @author Bruno Dufour
 * @see ArenaDeleteEvent
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class ArenaNewEvent extends Event implements ArenaEvent {
    /** The ID of the arena being created. */ 
    private int arena_id;
    /** The name of the arena being created. */
    private String arena_name;

    public ArenaNewEvent() {
        super(Event.ARENA_NEW);
        this.reset();
    }
    
    public ArenaNewEvent(int env_id, int arena_id, String arena_name) {
        this(env_id, arena_id, arena_name, false);
    }
    
    public ArenaNewEvent(int env_id, int arena_id, String name,
            boolean requested) {
        super(Event.ARENA_NEW, env_id, requested);
        this.arena_id = arena_id;
        this.arena_name = name;
    }
    
    public int getArenaID() {
        return this.arena_id;
    }
    
    public void setArenaID(int arena_id) {
        this.arena_id = arena_id;
    }

    public String getArenaName() {
        return this.arena_name;
    }
    
    public void setArenaName(String arena_name) {
        this.arena_name = arena_name;
    }
    
    public void readFromStream(TraceInput in, int mask, boolean requested)
            throws IOException {
        super.readFromStream(in, mask, requested);

        if ((mask & Constants.FIELD_ARENA_ID) != 0) {
            this.arena_id = in.readArenaID();
        } 
        if ((mask & Constants.FIELD_ARENA_NAME) != 0) {
            this.arena_name = in.readUTF();
        }
    }

    public void reset() {
        super.reset();
        
        this.arena_id = 0;
        this.arena_name = null;
    }
}

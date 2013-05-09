package starj.events;

import java.io.IOException;

import starj.spec.Constants;
import starj.io.traces.TraceInput;

/**
 * An abstract event class which is the base class for all *J events.
 * This class defines the various *J Event type IDs notably used as
 * array indices in the *J framework.
 * 
 * @author Bruno Dufour
 */
public abstract class Event implements JVMPIEvent, Cloneable {
    /**
     * The environment ID in which this event has been generated. This
     * information is typically obtained from the JVMPI Agent.
     */
    private int env_id;

    /**
     * The *J event type ID of the current event. 
     */
    private int id = UNKNOWN;

    /**
     * Indicates whether the event was explicitly requested by the JVMPI Agent.
     */
    private boolean requested = false;

    /**
     * The total number of events that are known to the *J framework. 
     * Valid event type IDs range from 0 to EVENT_COUNT - 1,
     * which allows their use as array indices.
     */
    public final static int EVENT_COUNT = 37;

    /**
     * The *J Event Type ID indicating an unknown event or an error.
     */
    public final static int UNKNOWN                       = -1;
    
    /**
     * Event type IDs
     */
    public final static int ARENA_DELETE                  =  0;
    public final static int ARENA_NEW                     =  1;
    public final static int CLASS_LOAD                    =  2;
    public final static int CLASS_LOAD_HOOK               =  3;
    public final static int CLASS_UNLOAD                  =  4;
    public final static int COMPILED_METHOD_LOAD          =  5;
    public final static int COMPILED_METHOD_UNLOAD        =  6;
    public final static int DATA_DUMP_REQUEST             =  7;
    public final static int DATA_RESET_REQUEST            =  8;
    public final static int GC_FINISH                     =  9;
    public final static int GC_START                      = 10;
    public final static int HEAP_DUMP                     = 11;
    public final static int JNI_GLOBALREF_ALLOC           = 12;
    public final static int JNI_GLOBALREF_FREE            = 13;
    public final static int JNI_WEAK_GLOBALREF_ALLOC      = 14;
    public final static int JNI_WEAK_GLOBALREF_FREE       = 15;
    public final static int JVM_INIT_DONE                 = 16;
    public final static int JVM_SHUT_DOWN                 = 17;
    public final static int METHOD_ENTRY                  = 18;
    public final static int METHOD_ENTRY2                 = 19;
    public final static int METHOD_EXIT                   = 20;
    public final static int MONITOR_CONTENDED_ENTER       = 21;
    public final static int MONITOR_CONTENDED_ENTERED     = 22;
    public final static int MONITOR_CONTENDED_EXIT        = 23;
    public final static int MONITOR_DUMP                  = 24;
    public final static int MONITOR_WAIT                  = 25;
    public final static int MONITOR_WAITED                = 26;
    public final static int OBJECT_ALLOC                  = 27;
    public final static int OBJECT_DUMP                   = 28;
    public final static int OBJECT_FREE                   = 29;
    public final static int OBJECT_MOVE                   = 30;
    public final static int RAW_MONITOR_CONTENDED_ENTER   = 31;
    public final static int RAW_MONITOR_CONTENDED_ENTERED = 32;
    public final static int RAW_MONITOR_CONTENDED_EXIT    = 33;
    public final static int THREAD_END                    = 34;
    public final static int THREAD_START                  = 35;
    public final static int INSTRUCTION_START             = 36;
    
    public Event(int id) {
        this(id, 0);
    }
    
    public Event(int id, int env_id) {
        this(id, env_id, false);
    }

    public Event(int id, int env_id, boolean requested) {
        this.reset();
        this.id = id;
        this.env_id = env_id;
        this.requested = requested;
    }
    
    public int getEnvID() {
        return this.env_id;
    }
    
    public void setEnvID(int env_id) {
        this.env_id = env_id;
    }

    public int getID() {
        return this.id;
    }
    
    public void setID(int id) {
        this.id = id;
    }

    public boolean isRequested() {
        return this.requested;
    }

    public void setRequested(boolean requested) {
        this.requested = requested;
    }

    public void readFromStream(TraceInput input, int mask, boolean requested)
            throws IOException {
        this.reset();

        this.requested = requested;
        if ((mask & Constants.FIELD_ENV_ID) != 0) {
            this.env_id = input.readThreadID();
        } 
    }

    public void reset() {
        this.requested = false;
        this.env_id = 0;
    }
}

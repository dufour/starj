package starj.events;

import java.io.IOException;

import starj.io.traces.TraceInput;
import starj.spec.Constants;

/**
 * The base class for monitor events.
 */
public abstract class MonitorEvent extends Event implements ObjectEvent {
    /** The object ID associated with the monitor. */
    private int object;

    public MonitorEvent(int id) {
       super(id);
       this.reset();
    }

    public MonitorEvent(int id, int env_id, int object) {
        this(id, env_id, object, false);
    }

    public MonitorEvent(int id, int env_id, int object, boolean requested) {
        super(id, env_id, requested);
        this.object = object;
    }
     
    public int getObjectID() {
        return this.object;
    }
     
    public void setObjectID(int object) {
        this.object = object;
    }

    public void readFromStream(TraceInput in, int mask, boolean requested)
            throws IOException {
        super.readFromStream(in, mask, requested);
        
        if ((mask & Constants.FIELD_OBJECT) != 0) {
            this.object = in.readObjectID();
        }
    }

    public void reset() {
        super.reset();

        this.object = 0;
    }
}

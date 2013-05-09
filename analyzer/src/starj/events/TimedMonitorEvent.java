package starj.events;

import java.io.IOException;

import starj.io.traces.TraceInput;
import starj.spec.Constants;

public abstract class TimedMonitorEvent extends MonitorEvent {
    /**
     * The number of milliseconds the thread is willing to wait (0 = no limit).
     */
    private long timeout;

    public TimedMonitorEvent(int id) {
        super(id);
    }

    public TimedMonitorEvent(int id, int env_id, int object, long timeout) {
        this(id, env_id, object, timeout, false);
    }

    public TimedMonitorEvent(int id, int env_id, int object, long timeout,
            boolean requested) {
        super(id, env_id, object, requested);
        this.timeout = timeout;
    }
    
    public long getTimeout() {
        return this.timeout;
    }
    
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
    
    public void readFromStream(TraceInput in, int mask, boolean requested)
            throws IOException {
        super.readFromStream(in, mask, requested);

        if ((mask & Constants.FIELD_TIMEOUT) != 0) {
            this.timeout = in.readLong();
        }
    }

    public void reset() {
        super.reset();

        this.timeout = -1L;
    }
}

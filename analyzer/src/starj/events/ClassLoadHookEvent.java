package starj.events;

import java.io.IOException;

import starj.spec.Constants;
import starj.io.traces.TraceInput;

/**
 * An Event corresponding to the <code>JVMPI_CLASS_LOAD_HOOK</code> event. This
 * event is triggered before the representation of a class is created by the
 * Java VM.
 *
 * @author Bruno Dufour
 * @see ClassLoadEvent
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class ClassLoadHookEvent extends Event {
    private byte[] class_data;
    private int class_data_len;

    public ClassLoadHookEvent() {
        super(Event.CLASS_LOAD_HOOK);
        this.reset();
    }
    
    public ClassLoadHookEvent(byte[] class_data) {
        super(Event.CLASS_LOAD_HOOK);
        this.class_data = class_data;
        if (class_data != null) {
            this.class_data_len = class_data.length;
        } else {
            this.class_data_len = 0;
        }
    }
    
    public ClassLoadHookEvent(int class_data_len) {
        super(Event.CLASS_LOAD_HOOK);
        this.class_data_len = class_data_len;
        this.class_data = null;
    }

    public int getClassDataLen() {
        return this.class_data_len;
    }

    public byte[] getClassData() {
        return this.class_data;
    }
    
    public void readFromStream(TraceInput in, int mask, boolean requested)
            throws IOException {
        super.readFromStream(in, mask, requested);

        if ((mask & Constants.FIELD_CLASS_DATA_LEN) != 0
                || (mask & Constants.FIELD_CLASS_DATA) != 0) {
            this.class_data_len = in.readInt();
        }
        
        if ((mask & Constants.FIELD_CLASS_DATA) != 0 
                && this.class_data_len > 0) {
            this.class_data = new byte[this.class_data_len];
            in.readFully(this.class_data);
        }
    }

    public void reset() {
        super.reset();
        
        this.class_data = null;
        this.class_data_len = 0;
    }
}

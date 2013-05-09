package starj.events;

import java.io.IOException;

import starj.coffer.InvokeInstruction;
import starj.io.traces.TraceInput;
import starj.spec.Constants;
import starj.toolkits.services.InstructionContext;

/**
 * An abstract event class corresponding to method entries.
 *
 * @author Bruno Dufour
 * @see MethodEntry2Event
 * @see MethodExitEvent
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public abstract class AbstractMethodEntryEvent extends Event
        implements MethodEvent {
    /** The ID of the method which is entered. */
    private int method_id;
    private InstructionContext call_site_context;

    public AbstractMethodEntryEvent(int event_id) {
        super(event_id);
        this.reset();
    }
    
    public AbstractMethodEntryEvent(int event_id, int env_id, int method_id) {
        this(event_id, env_id, method_id, false);
    }

    public AbstractMethodEntryEvent(int event_id, int env_id, int method_id,
            boolean requested) {
        super(event_id, env_id, requested);
        this.method_id = method_id;
    }
    
    public int getMethodID() {
        return this.method_id;
    }
    
    public void setMethodID(int method_id) {
        this.method_id = method_id;
    }
    
    public InvokeInstruction getCallSite() {
        InstructionContext context = this.call_site_context;
        if (context != null) {
            return (InvokeInstruction) context.getInstruction();
        }
        
        return null;
    }
    
    public InstructionContext getCallSiteContext() {
        return this.call_site_context;
    }
    
    public void setCallSiteContext(InstructionContext context) {
        this.call_site_context = context;
    }

    public void readFromStream(TraceInput in, int mask, boolean requested)
            throws IOException {
        super.readFromStream(in, mask, requested);

        if ((mask & Constants.FIELD_METHOD_ID) != 0) {
            this.method_id = in.readMethodID();
        }
    }

    public void reset() {
        super.reset();

        this.method_id = 0;
    }
}

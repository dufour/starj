package starj.io.traces;

import java.io.IOException;
import starj.events.Event;

public interface TraceReader {
    public short getMinorVersion();
    public short getMajorVersion();

    public boolean supports(short major_version, short minor_version);
    
    public int getFieldMask(int event_id);
    
    public int getAttributeCount();
    public boolean hasAttribute(String name);
    public TraceAttribute getAttribute(String name);
    public TraceAttribute[] getAttributes();
    
    public Event getNextEvent() throws TraceIOException, IOException;
    public long getCurrentEventCount();
}

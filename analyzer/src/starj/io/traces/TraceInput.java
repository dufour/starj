package starj.io.traces;

import java.io.*;

public interface TraceInput extends DataInput {
    public int readEventID() throws IOException;
    public int readFieldMask() throws IOException;
    public int readThreadID() throws IOException;
    public int readObjectID() throws IOException;
    public int readClassID() throws IOException;
    public int readMethodID() throws IOException;
    public int readArenaID() throws IOException;
    public int readJNIRefID() throws IOException;
    public int readRawMonitorID() throws IOException;
    public int readInstructionOffset() throws IOException;
    public int readLineno() throws IOException;
}

package starj.io.traces;

import java.io.IOException;

public class GenericTraceAttribute extends TraceAttribute {
    private byte[] data;
    
    public GenericTraceAttribute(String name, int length, byte[] data) {
        super(name, length);
        this.data = data;
    }

    public GenericTraceAttribute(String name, int length,
            TraceInput input) throws TraceIOException, IOException {
        super(name, length);
        this.data = new byte[length];
        input.readFully(this.data);
    }

    public byte[] getData() {
        return this.data;
    }
}

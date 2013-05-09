package starj.io.traces;

import java.io.*;

public class TraceInputStream extends DataInputStream implements TraceInput {
    private int ptr_size;
    private boolean short_offsets;
    
    public TraceInputStream(InputStream in) {
        this(in, false);
    }
    
    public TraceInputStream(InputStream in, boolean int_offsets) {
        this(in, int_offsets, 4);
    }

    public TraceInputStream(InputStream in, boolean int_offsets, int ptr_size) {
        super(in);
        this.setPointerSize(ptr_size);
        this.short_offsets = !int_offsets;
    }

    public boolean hasIntOffsets() {
        return !this.short_offsets;
    }

    public void setIntOffsets(boolean int_offsets) {
        this.short_offsets = !int_offsets;
    }

    public int getPointerSize() {
        return this.ptr_size;
    }

    public void setPointerSize(int ptr_size) {
        switch (ptr_size) {
            case 1:
            case 2:
            case 4:
                this.ptr_size = ptr_size;
                break;
            case 8:
                throw new RuntimeException("Only pointer sizes <=4 is supported at this time");
            default:
                throw new RuntimeException("Invalid pointer size: " + ptr_size);
        }
    }

    public int readEventID() throws IOException {
        return readUnsignedByte();
    }

    public int readFieldMask() throws IOException{
        return readInt();
    }

    public int readPointer() throws IOException {
        switch (this.ptr_size) {
            case 1:
                return read();
            case 2:
                return readShort();
            case 4:
                return readInt();
            default:
                throw new RuntimeException("Invalid pointer size");
        }
    }

    public int readThreadID() throws IOException {
        return readPointer();
    }

    public int readObjectID() throws IOException {
        return readPointer();
    }

    public int readClassID() throws IOException {
        return readPointer();
    }

    public int readMethodID() throws IOException {
        return readPointer();
    }

    public int readArenaID() throws IOException {
        return readInt();
    }

    public int readJNIRefID() throws IOException {
        return readPointer();
    }

    public int readRawMonitorID() throws IOException {
        return readPointer();
    }

    public int readInstructionOffset() throws IOException {
        if (this.short_offsets) {
            return readUnsignedShort();
        } else {
            return readInt();
        }
    }

    public int readLineno() throws IOException {
        return this.readInt();
    }
}

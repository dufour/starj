package starj.io.traces;

import java.io.IOException;

public class PointerSizeAttribute extends TraceAttribute {
    private int pointer_size;
    
    public PointerSizeAttribute(String name, int pointer_size) {
        super(name, 4);
        this.pointer_size = pointer_size;
    }

    public PointerSizeAttribute(String name, int length,
            TraceInput input) throws TraceIOException, IOException {
        super(name, length);
        if (length != 4) {
            throw new TraceFormatException("Invalid attribute length: "
                    + length);
        }

        this.pointer_size = input.readInt();
    }

    public int getPointerSize() {
        return this.pointer_size;
    }
}

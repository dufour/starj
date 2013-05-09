package starj.events;

import java.io.IOException;

import starj.io.traces.TraceInput;

/**
 * A class corresponding to the <code>JVMPI_Lineno</code> structure. This
 * structure represents a mapping between source line number and offset from
 * the beginning of a compiled method. This class is used by the
 * {@link starj.events.CompiledMethodLoadEvent CompiledMethodLoadEvent} class.
 *
 * @author Bruno Dufour
 * @see starj.events.CompiledMethodLoadEvent
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class JVMPILineno {
    /** The offset from the beginning of the method */
    private int offset;
    /** The line number from the beginning of the source file */
    private int lineno;

    public JVMPILineno() {
        this.reset();
    }

    public JVMPILineno(int offset, int lineno) {
        this.offset = offset;
        this.lineno = lineno;
    }

    public JVMPILineno(TraceInput in) throws IOException {
        this.offset = in.readInstructionOffset();
        this.lineno = in.readLineno();
    }

    public int getOffset() {
        return this.offset;
    }

    public int getLineno() {
        return this.lineno;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setLineno(int lineno) {
        this.lineno = lineno;
    }

    public void reset() {
        this.offset = -1;
        this.lineno = -1;
    }
}

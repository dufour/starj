package starj.events;

import java.io.IOException;

import starj.io.traces.TraceInput;

/**
 * A class corresponding to the <code>JVMPI_CallFrame</code> structure. This
 * structure represents a method being executed. This class is used by the
 * {@link starj.events.JVMPICallTrace CallTrace} class.
 *
 * @author Bruno Dufour
 * @see JVMPICallTrace
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class JVMPICallFrame {
    /** The line number in the source file */
    private int lineno;
    /** The method executed in this frame */
    private int method_id;

    public JVMPICallFrame() {
        this.reset();
    }

    public JVMPICallFrame(int lineno, int method_id) {
        this.lineno = lineno;
        this.method_id = method_id;
    }

    public JVMPICallFrame(TraceInput in) throws IOException {
        this.lineno = in.readLineno();
        this.method_id = in.readMethodID();
    }
    
    public int getLineno() {
        return this.lineno;
    }

    public int getMethodID() {
        return this.method_id;
    }
    
    public void setLineno(int lineno) {
        this.lineno = lineno;
    }

    public void setMethodID(int method_id) {
        this.method_id = method_id;
    }

    public void reset() {
        this.lineno = -1;
        this.method_id = 0;
    }
}

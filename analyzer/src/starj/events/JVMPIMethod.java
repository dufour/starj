package starj.events;

import java.io.IOException;

import starj.io.traces.TraceInput;

/**
 * A class corresponding to the <code>JVMPI_Method</code> structure. This
 * structure represents a method defined in a class. This class is used by the
 * {@link starj.events.ClassLoadEvent ClassLoadEvent} class.
 *
 * @author Bruno Dufour
 * @see starj.events.ClassLoadEvent
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class JVMPIMethod {
    /** The name of the method */
    private String method_name;
    /** The signature of the method */
    private String method_signature;
    /** The starting line number in the source file */
    private int start_lineno;
    /** The ending line number in the source file */
    private int end_lineno;
    /** The ID given to this method */
    private int method_id;

    public JVMPIMethod() {
        this.reset();
    }

    public JVMPIMethod(String method_name, String method_signature, 
            int start_lineno, int end_lineno, int method_id) {
        this.method_name = method_name;
        this.method_signature = method_signature;
        this.start_lineno = start_lineno;
        this.end_lineno = end_lineno;
        this.method_id = method_id;
    }

    public JVMPIMethod(TraceInput in) throws IOException {
        this.method_name = in.readUTF();
        this.method_signature = in.readUTF();
        this.start_lineno = in.readLineno();
        this.end_lineno = in.readLineno();
        this.method_id = in.readMethodID();
    }

    public String getMethodName() {
        return this.method_name;
    }

    public String getMethodSignature() {
        return this.method_signature;
    }

    public int getStartLineno() {
        return this.start_lineno;
    }

    public int getEndLineno() {
        return this.end_lineno;
    }

    public int getMethodID() {
        return this.method_id;
    }

    public void setMethodName(String method_name) {
        this.method_name = method_name;
    }

    public void setMethodSignature(String method_signature) {
        this.method_signature = method_signature;
    }

    public void setStartLineno(int start_lineno) {
        this.start_lineno = start_lineno;
    }

    public void setEndLineno(int end_lineno) {
        this.end_lineno = end_lineno;
    }

    public void setMethodID(int method_id) {
        this.method_id = method_id;
    }

    public void reset() {
        this.method_name = null;
        this.method_signature = null;
        this.start_lineno = -1;
        this.end_lineno = -1;
        this.method_id = 0;
    }
}

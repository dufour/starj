package starj.events;

import java.io.IOException;

import starj.io.traces.TraceInput;

/**
 * A class corresponding to the <code>JVMPI_Field</code> structure. This
 * structure represents a field defined in a class. This class is used by the
 * {@link starj.events.ClassLoadEvent ClassLoadEvent} class.
 *
 * @author Bruno Dufour
 * @see starj.events.ClassLoadEvent
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class JVMPIField {
    /** The name of the field */
    private String field_name;
    /** The signature of the field */
    private String field_signature;
    
    public JVMPIField() {
        this.reset();
    }

    public JVMPIField(String field_name, String field_signature) {
        this.field_name = field_name;
        this.field_signature = field_signature;
    }

    public JVMPIField(TraceInput in) throws IOException {
        this.field_name = in.readUTF();
        this.field_signature = in.readUTF();
    }

    public String getFieldName() {
        return this.field_name;
    }

    public String getFieldSignature() {
        return this.field_signature;
    }

    public void setFieldName(String field_name) {
        this.field_name = field_name;
    }

    public void setFieldSignature(String field_signature) {
        this.field_signature = field_signature;
    }

    public void reset() {
        this.field_name = null;
        this.field_signature = null;
    }
}

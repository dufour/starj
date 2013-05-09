package starj.events;

import java.io.IOException;

import starj.spec.Constants;
import starj.io.traces.TraceInput;

/**
 * An Event corresponding to the <code>JVMPI_COMPILED_METHOD_LOAD</code> event.
 * This event is triggered when a method is compiled an loaded into memory by
 * the Java VM.
 *
 * @author Bruno Dufour
 * @see CompiledMethodUnloadEvent
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class CompiledMethodLoadEvent extends Event implements MethodEvent {
    /** The ID of the method being loaded. */
    private int method_id;
    /** The size of the code of the method being loaded. */
    private int code_size;
    /** The code of the method being loaded. */
    private byte[] code;
    /** The size of the line number table of the method being loaded. */
    private int lineno_table_size;
    /** The line number table of the method being loaded. */
    private JVMPILineno[] lineno_table;
    
    public CompiledMethodLoadEvent() {
        super(Event.COMPILED_METHOD_LOAD);
        this.reset();
    }
    
    public CompiledMethodLoadEvent(int env_id, int method_id, byte[] code,
            JVMPILineno[] lineno_table, boolean requested) {
        super(Event.COMPILED_METHOD_LOAD, env_id, requested);
        this.method_id = method_id;
        this.code_size = (code != null ? code.length : 0);
        this.code = code;
        this.lineno_table_size
                = (lineno_table != null ? lineno_table.length : 0);
        this.lineno_table = lineno_table;
    }
    
    public int getMethodID() {
        return this.method_id;
    }
    
    public void setMethodID(int method_id) {
        this.method_id = method_id;
    }

    public int getCodeSize() {
        return this.code_size;
    }
    
    public byte[] getCode() {
        return this.code;
    }
    
    public byte getCode(int index) {
        return this.code[index];
    }
    
    public void setCode(byte[] code) {
        this.code = code;
        if (this.code == null) {
            this.code_size = 0;
        } else {
            this.code_size = code.length;
        }
    }
    
    public void setCode(byte code, int index) {
        this.code[index] = code;
    }
    
    public int getLinenoTableSize() {
        return this.lineno_table_size;
    }
    
    public JVMPILineno[] getLinenoTable() {
        return this.lineno_table;
    }
    
    public JVMPILineno getLinenoTable(int index) {
        return this.lineno_table[index];
    }
    
    public void setLinenoTable(JVMPILineno[] lineno_table) {
        this.lineno_table = lineno_table;
        if (this.lineno_table == null) {
            this.lineno_table_size = 0;
        } else {
            this.lineno_table_size = lineno_table.length;
        }
    }
    
    public void setLinenoTable(JVMPILineno lineno_table, int index) {
        this.lineno_table[index] = lineno_table;
    }
    
    public void readFromStream(TraceInput in, int mask, boolean requested)
            throws IOException {
        super.readFromStream(in, mask, requested);

        if ((mask & Constants.FIELD_METHOD_ID) != 0) {
            this.method_id = in.readMethodID();
        }

        if ((mask & Constants.FIELD_CODE_SIZE) != 0
                || (mask & Constants.FIELD_CODE) != 0) {
            this.code_size = in.readInt();
        }
        
        if ((mask & Constants.FIELD_CODE) != 0
                && this.code_size > 0) {
            this.code = new byte[this.code_size];
            in.readFully(this.code);
        }
        
        if ((mask & Constants.FIELD_LINENO_TABLE_SIZE) != 0
                || (mask & Constants.FIELD_LINENO_TABLE) !=  0) {
            this.lineno_table_size = in.readInt();
        }
        
        if ((mask & Constants.FIELD_LINENO_TABLE) != 0
                && this.lineno_table_size > 0) {
            this.lineno_table = new JVMPILineno[this.lineno_table_size];
            for (int i = 0; i < this.lineno_table_size; i++) {
                this.lineno_table[i] = new JVMPILineno(in);
            }
        }
    }

    public void reset() {
        super.reset();
        
        this.method_id = 0;
        
        this.code_size = 0;
        this.code = null;
        
        this.lineno_table_size = 0;
        this.lineno_table = null;
    }
}

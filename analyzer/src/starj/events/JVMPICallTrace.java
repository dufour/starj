package starj.events;

import java.io.IOException;

import starj.io.traces.TraceInput;

/**
 * A class corresponding to the <code>JVMPI_CallTrace</code> structure. This
 * structure represents a call trace of a method execution. 
 *
 * @author Bruno Dufour
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class JVMPICallTrace {
    /** The ID of the thread which executed this trace.*/
    private int env_id;
    /**
     * The number of frames in the trace. This value is normally equal to
     * <code>frames.length</code>
     */
    private int num_frames;
    /** The <code>JVMPICallFrame</code> objects that make up this trace
     * (callee followed by callers)
     */
    private JVMPICallFrame[] frames;

    public JVMPICallTrace() {
        this.reset();
    }

    public JVMPICallTrace(int env_id, JVMPICallFrame[] frames) {
        this.env_id = env_id;
        this.num_frames = (frames != null ? frames.length : 0);
        this.frames = frames;
    }

    public JVMPICallTrace(TraceInput in) throws IOException {
        this.env_id = in.readThreadID();
        this.num_frames = in.readInt();
        if (this.num_frames > 0) {
            this.frames = new JVMPICallFrame[this.num_frames];
            for (int j = 0; j < this.num_frames; j++) {
                this.frames[j] = new JVMPICallFrame(in);
            }
        }
    }
    
    public int getEnvID() {
        return this.env_id;       
    }

    public int getNumFrames() {
        return this.num_frames;
    }

    public JVMPICallFrame[] getFrames() {
        return this.frames;
    }

    public JVMPICallFrame getFrame(int index) {
        return this.frames[index];
    }

    public void setEnvID(int env_id) {
        this.env_id = env_id;
    }

    public void setFrames(JVMPICallFrame[] frames) {
        this.frames = frames;
    }

    public void setFrame(JVMPICallFrame frame, int index) {
        this.frames[index] = frame;
    }

    public void reset() {
        this.env_id = 0;
        this.num_frames = 0;
        this.frames = null;
    }
}

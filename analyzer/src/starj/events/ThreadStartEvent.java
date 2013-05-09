package starj.events;

import java.io.IOException;

import starj.io.traces.TraceInput;
import starj.spec.Constants;

/**
 * An Event corresponding to the <code>JVMPI_THREAD_START</code> event. This
 * event is triggered when a thread is started in the Java VM.
 *
 * @author Bruno Dufour
 * @see ThreadEndEvent
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class ThreadStartEvent extends Event implements ThreadEvent {
    /** The name of the thread being started. */
    private String thread_name;
    /** The name of the group to wich the thread belongs. */
    private String group_name;
    /** The name of the parent of the thread being started. */
    private String parent_name;
    /** The object ID of the thread being started. */
    private int thread_id;
    /** The environment ID of the thread being started. */
    private int thread_env_id;

    public ThreadStartEvent() {
        super(Event.THREAD_START);
        this.reset();
    }

    public ThreadStartEvent(int env_id, String thread_name, String group_name,
            String parent_name, int thread_id, int thread_env_id) {
        this(env_id, thread_name, group_name, parent_name, thread_id,
                thread_env_id, false);
    }

    public ThreadStartEvent(int env_id, String thread_name, String group_name,
            String parent_name, int thread_id, int thread_env_id,
            boolean requested) {
        super(Event.THREAD_START, env_id, requested);
        this.thread_name = thread_name;
        this.group_name = group_name;
        this.parent_name = parent_name;
        this.thread_id = thread_id;
        this.thread_env_id = thread_env_id;
    }
    
    public String getThreadName() {
        return this.thread_name;
    }
    
    public void setThreadName(String thread_name) {
        this.thread_name = thread_name;
    }
    
    public String getGroupName() {
        return this.group_name;
    }
    
    public void setGroupName(String group_name) {
        this.group_name = group_name;
    }
    
    public String getParentName() {
        return this.parent_name;
    }
    
    public void setParentName(String parent_name) {
        this.parent_name = parent_name;
    }
    
    public int getThreadID() {
        return this.thread_id;
    }
    
    public void setThreadID(int thread_id) {
        this.thread_id = thread_id;
    }

    public int getThreadEnvID() {
        return this.thread_env_id;
    }
    
    public void setThreadEnvID(int thread_env_id) {
        this.thread_env_id = thread_env_id;
    }

    public void readFromStream(TraceInput in, int mask, boolean requested)
            throws IOException {
        super.readFromStream(in, mask, requested);

        if ((mask & Constants.FIELD_THREAD_NAME) != 0) {
            this.thread_name = in.readUTF();
        }
        
        if ((mask & Constants.FIELD_GROUP_NAME) != 0) {
            this.group_name = in.readUTF();
        }
        
        if ((mask & Constants.FIELD_PARENT_NAME) != 0) {
            this.parent_name = in.readUTF();
        }
        
        if ((mask & Constants.FIELD_THREAD_ID) != 0) {
            this.thread_id = in.readObjectID();
        }
        
        if ((mask & Constants.FIELD_THREAD_ENV_ID) != 0) {
            this.thread_env_id = in.readThreadID();
        }
    }

    public void reset() {
        super.reset();

        this.thread_name = null;
        this.group_name = null;
        this.parent_name = null;

        this.thread_id = 0;
        this.thread_env_id = 0;
    }
}

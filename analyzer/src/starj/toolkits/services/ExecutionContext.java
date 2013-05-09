package starj.toolkits.services;

import starj.events.MethodEvent;

public class ExecutionContext implements Cloneable {
    private ThreadEntity thread;
    private MethodEntity method;
    
    public ExecutionContext(MethodEvent event) {
        this(IDResolver.v().getThreadEntity(event.getEnvID()),
                IDResolver.v().getMethodEntity(event.getMethodID()));
    }
    
    public ExecutionContext(ThreadEntity thread, MethodEntity method) {
        this.thread = thread;
        this.method = method;
    }

    public ThreadEntity getThread() {
        return this.thread;
    }

    public MethodEntity getMethod() {
        return this.method;
    }

    void setThread(ThreadEntity thread) {
        this.thread = thread;
    }

    void setMethod(MethodEntity method) {
        this.method = method;
    }
    
    public Object clone()  {
        return new ExecutionContext(this.thread, this.method);
    }
    
    public int hashCode() {
        int thread_hc = (this.thread != null ? this.thread.hashCode() : 0);
        int method_hc = (this.method != null ? this.method.hashCode() : 0);
        
        return (thread_hc << 16) | method_hc;
    }
    
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (!(obj instanceof ExecutionContext)) {
            return false;
        }
        
        ExecutionContext oec = (ExecutionContext) obj;
        
        return (this.thread == oec.thread || (this.thread != null
                        && this.thread.equals(oec.thread)))
                        && (this.method == oec.method || (this.method != null
                                && this.method.equals(oec.method)));
    }

    // FIXME: toString ?
}

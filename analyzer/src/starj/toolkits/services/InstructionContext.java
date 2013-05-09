package starj.toolkits.services;

import starj.coffer.Instruction;
import starj.events.InstructionStartEvent;

public class InstructionContext implements Cloneable {
    private Instruction inst;
    private ExecutionContext context;
    
    public InstructionContext(Instruction inst, ExecutionContext context) {
        this.inst = inst;
        this.context = context;
    }
    
    public InstructionContext(InstructionStartEvent event) {
        this(event.getInstruction(), new ExecutionContext(event));
    }

    public Instruction getInstruction() {
        return this.inst;
    }

    public ExecutionContext getExecutionContext() {
        return this.context;
    }

    void setInstruction(Instruction inst) {
        this.inst = inst;
    }

    void setExecutionContext(ExecutionContext context) {
        this.context = context;
    }
    
    public Object clone() {
        return new InstructionContext(this.inst, this.context);
    }
    
    // Convenience methods
    
    public MethodEntity getMethod() {
        if (this.context != null) {
            return this.context.getMethod();
        }
        
        return null;
    }
    
    public ThreadEntity getThread() {
        if (this.context != null) {
            return this.context.getThread();
        }
        
        return null;
    }


    // TODO: equals, hashCode, etc.
}

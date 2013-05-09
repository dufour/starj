package starj.toolkits.services;

import starj.util.ObjectStack;

public class InvocationStack {
    private ObjectStack stack;
    
    public InvocationStack() {
        this.stack = new ObjectStack();
    }
    
    void push(StackFrame frame) {
        this.stack.push(frame);
    }
    
    StackFrame pop() {
        return (StackFrame) this.stack.pop();
    }
    
    public StackFrame getCurrentStackFrame() {
        return (StackFrame) this.stack.top();
    }
    
    public boolean isEmpty() {
        return this.stack.isEmpty();
    }
}

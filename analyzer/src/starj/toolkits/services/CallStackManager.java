package starj.toolkits.services;

import starj.AbstractOperation;
import starj.EventBox;
import starj.coffer.Method;
import starj.dependencies.*;
import starj.events.*;
import starj.io.logging.LogManager;
import starj.spec.Constants;
import starj.util.IntHashMap;

public class CallStackManager extends AbstractOperation implements Service {
    private static CallStackManager instance = new CallStackManager();
    private IntHashMap env_id_to_invocation_stack;

    private CallStackManager() {
        // no instances
        super("CallStackManager",
                "Resolves call sites for method entry events");
    }

    public static CallStackManager v() {
        return CallStackManager.instance;
    }
 
    public EventDependencySet eventDependencies() {
        EventDependencySet dep_set = new EventDependencySet();

        FieldMask method_mask = new TotalMask(
                Constants.FIELD_RECORDED
                | Constants.FIELD_METHOD_ID);
        dep_set.add(new EventDependency(
                Event.METHOD_ENTRY2,
                method_mask,
                true,
                new EventDependency(
                    Event.METHOD_ENTRY,
                    method_mask,
                    true)));
        dep_set.add(new EventDependency(
                Event.METHOD_EXIT,
                method_mask,
                true));

        return dep_set;
    }

    public void init() {
        this.env_id_to_invocation_stack = new IntHashMap(7);
    }

    public void apply(EventBox box) {
        Event event = box.getEvent();
        MethodEvent e = (MethodEvent) event;
        int method_id = e.getMethodID();
        MethodEntity me = IDResolver.v().getMethodEntity(method_id);
        //Method m = (me != null ? me.getMethod() : null);
        
        int env_id = e.getEnvID();
        InvocationStack inv_stack = (InvocationStack)
                this.env_id_to_invocation_stack.get(env_id);
        if (inv_stack == null) {
            inv_stack = new InvocationStack();
            this.env_id_to_invocation_stack.put(env_id, inv_stack);
        }
        
        switch (event.getID()) {
            case Event.METHOD_ENTRY:
            case Event.METHOD_ENTRY2: {
                    if (me == null) {
                        LogManager.v().logWarning(
                                "Entering an unresolved method");
                    }
                    inv_stack.push(new StackFrame(me));
                }
                break;
            case Event.METHOD_EXIT: {
                    if (!inv_stack.isEmpty()) {
                        StackFrame frame = inv_stack.getCurrentStackFrame();
                        if (frame.matches(me)) {
                            inv_stack.pop();
                        } else {
                            LogManager.v().logWarning("Invocation mismatch");
                            box.remove();
                            // Never reached
                        }
                    }
                }
                break;
        }
    }
    

    public ExecutionContext getCurrentExecutionContext(int env_id) {
        return new ExecutionContext(IDResolver.v().getThreadEntity(env_id),
                this.getCurrentMethodEntity(env_id));
    }
    
    public StackFrame getCurrentStackFrame(int env_id) {
        InvocationStack inv_stack = (InvocationStack)
                this.env_id_to_invocation_stack.get(env_id);
        if (inv_stack != null) {
            if (!inv_stack.isEmpty()) {
                return inv_stack.getCurrentStackFrame();
            }
        }
        
        return null;
    }
    
    public MethodEntity getCurrentMethodEntity(int env_id) {
        StackFrame f = this.getCurrentStackFrame(env_id);
        return (f != null ? f.getMethodEntity() : null);
    }
    
    public Method getCurrentMethod(int env_id) {
        StackFrame f = this.getCurrentStackFrame(env_id);
        return (f != null ? f.getMethod() : null);
    }
}

package starj.toolkits.services;

import starj.*;
import starj.coffer.InvokeInstruction;
import starj.dependencies.*;
import starj.events.*;
import starj.spec.Constants;

public abstract class PropagationOperation extends AbstractOperation 
        implements Propagation {
    private ExecutionContext execution_context; // Cache

    public PropagationOperation(String name, String description) {
        super(name, description);
        this.execution_context = new ExecutionContext(null, null);
    }

    public OperationSet operationDependencies() {
        OperationSet dep_set = super.operationDependencies();
        dep_set.add(CallSiteResolver.v());
        return dep_set;
    }

    public EventDependencySet eventDependencies() {
        EventDependencySet dep_set = super.eventDependencies();
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

    public void apply(EventBox box) {
        Event event = box.getEvent();
        int env_id = event.getEnvID();

        switch (event.getID()) {
            case Event.METHOD_ENTRY2:
            case Event.METHOD_ENTRY: {
                    AbstractMethodEntryEvent e
                            = (AbstractMethodEntryEvent) event;
                    int method_id = e.getMethodID();
                    MethodEntity me = IDResolver.v().getMethodEntity(method_id);
                    InstructionContext ic = e.getCallSiteContext();
                    ExecutionContext ec = this.execution_context;
                    ec.setMethod(ic != null ? ic.getMethod() : null);
                    ec.setThread(IDResolver.v().getThreadEntity(env_id));
                    this.propagate(e.getCallSite(), me, ec);
                }
                break;
            case Event.METHOD_EXIT: {
                    MethodExitEvent e = (MethodExitEvent) event;
                    int method_id = e.getMethodID();
                    MethodEntity me = IDResolver.v().getMethodEntity(method_id);
                    ExecutionContext ec = this.execution_context;
                    ec.setMethod(me);
                    ec.setThread(IDResolver.v().getThreadEntity(env_id));
                    this.unpropagate(ec);
                }
                break;
            default:
                break;
        }
    }

    public abstract void propagate(InvokeInstruction call_site,
            MethodEntity new_method, ExecutionContext call_context);
    public abstract void unpropagate(ExecutionContext context);
}

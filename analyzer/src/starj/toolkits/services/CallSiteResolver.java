package starj.toolkits.services;

import starj.AbstractOperation;
import starj.EventBox;
import starj.coffer.*;
import starj.dependencies.*;
import starj.events.*;
import starj.util.*;
import starj.io.logging.LogManager;
import starj.spec.Constants;

public class CallSiteResolver extends AbstractOperation implements Service {
    private static CallSiteResolver instance = new CallSiteResolver();
    private IntHashMap env_id_to_pending_inv_stack; // Map from thread env id
                                                    // to boolean stack
                                                    // indicating whether there
                                                    // is a pending call site
    private IntHashMap env_id_to_invoke_stack; // Map from thread env id
                                               // to stack of call site
                                               // information records

    private CallSiteResolver() {
        super("CallSiteResolver",
                "Resolves call sites for method entry events");
        // no instances
    }

    public static CallSiteResolver v() {
        return instance;
    }

    public OperationSet operationDependencies() {
        OperationSet dep_set = super.operationDependencies();
        dep_set.add(InstructionResolver.v());

        return dep_set;
    }

    public EventDependencySet eventDependencies() {
        EventDependencySet dep_set = new EventDependencySet();
        dep_set.add(new EventDependency(
                Event.INSTRUCTION_START,
                new TotalMask(
                        Constants.FIELD_RECORDED
                        |Constants.FIELD_METHOD_ID
                        | Constants.FIELD_OFFSET),
                true));
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
        this.env_id_to_pending_inv_stack = new IntHashMap(7);
        this.env_id_to_invoke_stack = new IntHashMap(7);
    }

    public void apply(EventBox box) {
        Event event = box.getEvent();
        int env_id = event.getEnvID();

        BooleanStack pending_inv_stack;
        if (this.env_id_to_pending_inv_stack.containsKey(env_id)) {
            pending_inv_stack
                    = (BooleanStack) this.env_id_to_pending_inv_stack.get(env_id);
        } else {
            pending_inv_stack = new BooleanStack();
            this.env_id_to_pending_inv_stack.put(env_id, pending_inv_stack);
        }

        ObjectStack invoke_stack;
        if (this.env_id_to_invoke_stack.containsKey(env_id)) {
            invoke_stack
                    = (ObjectStack) this.env_id_to_invoke_stack.get(env_id);
        } else {
            invoke_stack = new ObjectStack();
            this.env_id_to_invoke_stack.put(env_id, invoke_stack);
        }

        switch (event.getID()) {
            case Event.INSTRUCTION_START: {
                    InstructionStartEvent e = (InstructionStartEvent) event;

                    if (pending_inv_stack.peek(false)) {
                        // Whatever was invoked has been optimized away
                        if (!invoke_stack.isEmpty()) {
                            invoke_stack.pop();
                        }
                    }

                    switch (e.getOpcode()) {
                        case Code.INVOKEVIRTUAL:
                        case Code.INVOKEINTERFACE:
                        case Code.INVOKESTATIC:
                        case Code.INVOKESPECIAL:
                            invoke_stack.push(
                                    InstructionResolver.v().getCurrentContext(
                                            env_id));
                            pending_inv_stack.pushOrReplace(true);
                            break;
                        default:
                            pending_inv_stack.pushOrReplace(false);
                            break;
                    }
                }
                break;
            case Event.METHOD_ENTRY:
            case Event.METHOD_ENTRY2: {
                    AbstractMethodEntryEvent e
                            = (AbstractMethodEntryEvent) event;
                    int method_id = e.getMethodID();
                    MethodEntity me = IDResolver.v().getMethodEntity(method_id);
                    if (me == null) {
                        LogManager.v().logWarning("CallSiteResolver skipping "
                                + " unknown method with ID " + method_id);
                        box.remove();
                    }

                    InstructionContext context = null;
                    if (!invoke_stack.isEmpty()) {
                        context = (InstructionContext) invoke_stack.top();
                        InvokeInstruction inst
                                = (InvokeInstruction) context.getInstruction();
                        if (inst != null) {
                            Method target_method = inst.getTargetMethod();
                            if (this.invocationMatches(target_method,
                                    me.getMethod())) {
                                // Clear previous value
                                pending_inv_stack.pushOrReplace(false);
                                
                                // remove this call site from the call site
                                // stack
                                invoke_stack.pop();
                                
                                // Keep current call site context value
                            } else {
                                // The executed method is not the invoked method
                                // This should mean that were are executing part
                                // of the Class Loader now (or some other
                                // spontaneously executing piece of code).

                                // We have no call site for this method, so
                                // set the call site context back to null
                                context = null;
                            }
                        } else {
                            // There is no invoke instruction in the calling
                            // context => assume no call site for this method
                            context = null;
                        }
                    }

                    // Push a new value for the current method
                    e.setCallSiteContext(context);
                    pending_inv_stack.push(false);
                    me.enter(env_id);
                }
                break;                      
            case Event.METHOD_EXIT: {
                    MethodExitEvent e = (MethodExitEvent) event;
                    int method_id = e.getMethodID();
                    MethodEntity me = IDResolver.v().getMethodEntity(method_id);

                    if (me == null) {
                        LogManager.v().logWarning("CallSiteResolver skipping "
                                + " unknown method with ID " + method_id);
                        box.remove();
                    }

                    if (!me.exit(event.getEnvID())) {
                        // We have never entered this method...
                        // Continuing would cause serious problems
                        // with the stacks.
                        box.remove(); // will stop processing at this point
                        // -- Never reached, but the compiler does not know ;)
                        break; 
                    }

                    if (!pending_inv_stack.isEmpty()) {
                        pending_inv_stack.pop();
                    }
                }
                break;
            default:
                break;
        }
    }

    private boolean invocationMatches(Method invoke_decl, Method invoke_tgt) {
        // Try the simple case
        if (invoke_decl == invoke_tgt) {
            return true;
        }

        if (invoke_decl == null || invoke_tgt == null) {
            LogManager.v().logWarning(this.getClass().getName()
                    + " received a null method instance");
            return false;
        }

        // Sanity check, which can save us a fair amount of processing
        // in most cases
        String method_name = invoke_decl.getName();
        String method_sig = invoke_decl.getDescriptor();
        if (!method_name.equals(invoke_tgt.getName())
                || !method_sig.equals(invoke_tgt.getDescriptor())) {
            return false;
        }

        ClassFile decl_class = invoke_decl.getClassFile();
        ClassFile receiver_class = invoke_tgt.getClassFile();
        if (decl_class.equals(receiver_class)) {
            // Method is the same, so we are certain of equality here
            return true;
        }

        if (decl_class.isInterface()) {
            /* If we have an invokeinterface call, then the complexity of the
             * operation can be high.  The simplest (and most frequent) case
             * occurs when the dynamic receiver implements the interface
             * directly (case #1). However, this does not have to be the case
             * (case #2). For example, a call to
             * java.util.Set.iterator()Ljava/util/Iterator; may actually at
             * runtime be executed as
             * java.util.Collections$SynchronizedCollection.iterator()Ljava/util/Iterator;
             * (this is a real example), which complicates matters a bit. The
             * real receiver must have been a subclass of
             * SynchronizedCollection which implements java.util.Set (i.e. a
             * synchronized set), but which did not override the iterator()
             * method, resulting in such a dynamic behaviour. In that case, we
             * need to make sure that:
             *     1) Case #1 fails
             *     2) There exists a superinterface X of iface which defines
             *        the appropriate method, and the runtime receiver class
             *        implements that method.
             *
             * Luckily enough, lookupMethod() will take care of finding
             * the proper method in the class hierarchy, so all we have
             * to do here is make sure that the super class implements
             * the receiver method.
             */
            return receiver_class.isImplementationOf(decl_class);
        } else {
            // decl_class and receiver_class can't be equal, so check
            // for a subclass relationship
            return receiver_class.isSubclassOf(decl_class);
        }
        // Unreachable
    }
}

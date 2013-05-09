package starj.toolkits.services;

import starj.AbstractOperation;
import starj.EventBox;
import starj.coffer.*;
import starj.dependencies.*;
import starj.events.*;
import starj.io.logging.LogManager;
import starj.spec.Constants;
import starj.util.IntHashMap;

public class InstructionResolver extends AbstractOperation implements Service {
    private static InstructionResolver instance = new InstructionResolver();
    private long missed_count;
    private long inst_count;
    private IntHashMap thread_id_to_context;

    private InstructionResolver() {
        super("InstructionResolver",
                "Resolves bytecode instructions and opcodes from offsets");
        // no instances
    }

    public static InstructionResolver v() {
        return instance;
    }

    public OperationSet operationDependencies() {
        OperationSet dep_set = super.operationDependencies();
        dep_set.add(IDResolver.v());

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

        return dep_set;
    }

    public void init() {
        this.missed_count = 0L;
        this.inst_count = 0L;
        this.thread_id_to_context = new IntHashMap(7);
    }

    public void apply(EventBox box) {
        InstructionStartEvent e = (InstructionStartEvent) box.getEvent();
        MethodEntity mentity = IDResolver.v().getMethodEntity(e.getMethodID());
        Instruction inst;
        try {
            Method m = mentity.getMethod();
            Code c = m.getCode();
            inst = c.lookup(e.getOffset());
            short opcode = inst.getOpcode();
            e.setOpcode(opcode);
        } catch (NullPointerException npe) {
            e.setOpcode(Code.UNKNOWN);
            inst = null;
            this.missed_count++;
        }
        e.setInstruction(inst);
        
        int env_id = e.getEnvID();
        if (this.thread_id_to_context.containsKey(env_id)) {
            InstructionContext context
                    = (InstructionContext) this.thread_id_to_context.get(
                            env_id);
            context.setInstruction(inst);
            context.setExecutionContext(new ExecutionContext(e));
        } else {
            this.thread_id_to_context.put(env_id, new InstructionContext(e));
        }
        this.inst_count++;
    }
    
    public InstructionContext getCurrentContext(int env_id) {
        InstructionContext rv
                = (InstructionContext) this.thread_id_to_context.get(env_id);
        if (rv != null) {
            return (InstructionContext) rv.clone(); 
        }
        return null;
    }

    public long getInstructionCount() {
        return this.inst_count;
    }

    public void done() {
        if (this.missed_count > 0) {
            LogManager.v().logWarning(this.missed_count
                    + " instructions were left unresolved");
        }
    }
}

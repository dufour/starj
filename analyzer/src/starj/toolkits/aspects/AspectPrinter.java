package starj.toolkits.aspects;

import java.io.PrintStream;

import starj.*;
import starj.coffer.Instruction;
import starj.dependencies.*;
import starj.events.*;
import starj.spec.Constants;
import starj.toolkits.printers.AbstractPrinter;
import starj.toolkits.services.*;

public class AspectPrinter extends AbstractPrinter {
    protected PrintStream out;
    private String indent;

    public AspectPrinter(String name, String description) {
        super(name, description);
        this.indent = "";
    }
    
    public AspectPrinter(String name, String description, PrintStream out) {
        super(name, description, out);
        this.indent = "";
    }

    public OperationSet operationDependencies() {
        OperationSet set = super.operationDependencies();
        set.add(IDResolver.v());
        set.add(CallSiteResolver.v());
        set.add(AspectTagResolver.v());

        return set;
    }
    
    public EventDependencySet eventDependencies() {
        EventDependencySet dep_set = new EventDependencySet();
        
        FieldMask method_mask = new TotalMask(Constants.FIELD_RECORDED
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

        dep_set.add(new EventDependency(
                Event.INSTRUCTION_START,
                new TotalMask(Constants.FIELD_RECORDED),
                true));

        return dep_set;
    }

    public void apply(EventBox box) {
        Event event = box.getEvent();
        int id = event.getID();
        int env_id = event.getEnvID();
        if (id == Event.INSTRUCTION_START) {
            InstructionStartEvent e = (InstructionStartEvent) event;
            this.printInstruction(e.getInstruction(), env_id, "");
        } else {
            MethodEvent e = (MethodEvent) event;
            String prefix;
            if (id == Event.METHOD_EXIT) {
                prefix = "-";
                this.indent = this.indent.substring(2);
            } else {
                prefix = "+";
            }
            MethodEntity me = IDResolver.v().getMethodEntity(e.getMethodID());
            this.out.println(this.indent + prefix + ": " + me);
            this.out.println(this.indent + "D: "
                    + AspectTagResolver.v().getAspectCodeDepth(env_id));

            if (id != Event.METHOD_EXIT) {
                this.printInstruction(
                        ((AbstractMethodEntryEvent) e).getCallSite(),
                        env_id,
                        "CS: ");
                this.indent += "  ";
            }
        }
    }

    public void printInstruction(Instruction inst, int env_id,
            String prefix) {
        if (inst != null) {
            AspectTagResolver r = AspectTagResolver.v();
            this.out.println(this.indent + prefix + "<"
                    + r.getCurrentKindTag(inst, env_id)
                    + "/"
                    + r.getStaticKindTag(inst)
                    + "> " + inst);
        } else {
            this.out.println(this.indent + "<?/?> (null)");
        }
    }
}

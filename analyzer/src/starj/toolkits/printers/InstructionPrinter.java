package starj.toolkits.printers;

import java.io.PrintStream;

import starj.*;
import starj.dependencies.*;
import starj.events.*;
import starj.spec.Constants;
import starj.toolkits.services.*;

public class InstructionPrinter extends AbstractPrinter {
    public InstructionPrinter(String name, String description) {
        super(name, description);
    }
    
    public InstructionPrinter(String name, String description,
            PrintStream out) {
        super(name, description, out);
    }
    
    public OperationSet operationDependencies() {
        OperationSet dep_set = super.operationDependencies();
        dep_set.add(IDResolver.v());
        dep_set.add(InstructionResolver.v());

        return dep_set;
    }

    public EventDependencySet eventDependencies() {
        EventDependencySet dep_set = new EventDependencySet();
        dep_set.add(new EventDependency(
                Event.INSTRUCTION_START,
                new TotalMask(Constants.FIELD_RECORDED
                              | Constants.FIELD_OFFSET),
                true));
        FieldMask method_mask = new TotalMask(
                Constants.FIELD_RECORDED
                | Constants.FIELD_METHOD_ID);
        dep_set.add(new EventDependency(
                Event.METHOD_ENTRY2,
                method_mask,
                false,
                new EventDependency(
                    Event.METHOD_ENTRY,
                    method_mask,
                    false)));
        dep_set.add(new EventDependency(
                Event.METHOD_EXIT,
                method_mask,
                false));

        return dep_set;
    }

    public void apply(EventBox box) {
        Event event = box.getEvent();

        switch (event.getID()) {
            case Event.INSTRUCTION_START: {
                    InstructionStartEvent e = (InstructionStartEvent) event;
                    this.out.println(e.getInstruction());
                }
                break;
            case Event.METHOD_ENTRY:
            case Event.METHOD_ENTRY2: {
                    MethodEvent e = (MethodEvent) event;
                    MethodEntity me
                            = IDResolver.v().getMethodEntity(e.getMethodID());
                    this.out.println("->" + me);
                }
                break;
            case Event.METHOD_EXIT: {
                    MethodEvent e = (MethodEvent) event;
                    MethodEntity me
                            = IDResolver.v().getMethodEntity(e.getMethodID());
                    this.out.println("<-" + me);
                }
                break;
            default:
                break;
        }
    }
}
